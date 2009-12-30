package org.gwt.speedchart.client.data;

import org.gwt.speedchart.client.Dataset;
import org.gwt.speedchart.client.MutableDataset;
import org.gwt.speedchart.client.data.Mutation.AppendMutation;
import org.gwt.speedchart.client.data.tuple.Tuple2D;
import org.gwt.speedchart.client.util.ArgChecker;
import org.gwt.speedchart.client.util.Interval;
import org.gwt.speedchart.client.util.IntervalSet;
import org.gwt.speedchart.client.util.Array1D;
import org.gwt.speedchart.client.util.JavaArray1D;
import org.gwt.speedchart.client.util.Util;

import java.util.ArrayList;
import java.util.List;

import com.allen_sauer.gwt.log.client.Log;


/**
 * Dataset that permits that large amouts of new data points is
 * inserted at random positions in the set.
 *
 * The initial data points specifies the outer domain bounds of the
 * dataset.
 */

public class ZoomDataset2D extends AbstractDataset<Tuple2D> {
   
  public static final class Request {

    private Interval region;

    private int unitIdx;

    public Request(Interval region, int unitIdx) {
      this.region = region;
      this.unitIdx = unitIdx;
    }

    public Interval getRegion() {
      return region;
    }

    public int getUnitIdx() {
      return unitIdx;
    }

  }

  /**
   * Callback interface used by implementors of {@link DatasetModel}
   * to provide data.
   */
  public interface Callback {
    void provideData(double[] domain, double[] range);
  }

  public interface DatasetModel {

    double[] getUnits();

    void requestData(Request request, Callback callback);

  }

  private class DelegateCallback implements Callback {

    int unitIdx;

    public DelegateCallback(int unitIdx) {
      this.unitIdx = unitIdx;
    }

    public void provideData(double[] domain, double[] range) {
      ZoomDataset2D.this.provideData(unitIdx, domain, range);
    }
  }

  private DatasetModel model;

  protected double minDomainInterval;

  private List<DatasetListener<Tuple2D>> listeners 
      = new ArrayList<DatasetListener<Tuple2D>>();

  private final Interval domainExtrema;

  private MipMap[] mipMaps;

  private final MipMap extremaMipMap;

  private final double[] unitWidths;

  private final int numUnits;
  
  private final IntervalSet[] intervalSets;

  public ZoomDataset2D(DatasetModel model, Interval domainExtrema) {

    this.model = model;
    this.domainExtrema = domainExtrema;
    unitWidths = model.getUnits();
    numUnits = this.unitWidths.length;

    intervalSets = new IntervalSet[numUnits];
    mipMaps = new MipMap[numUnits];
    extremaMipMap = createExtremaMipMap(domainExtrema);
  }

  private MipMap createExtremaMipMap(Interval domainExtrema) {
    double[] domain = new double[2];
    domain[0] = domainExtrema.getStart();
    domain[1] = domainExtrema.getEnd();

    double[] range  = new double[2];
    range[0] = range[1] = 0;
    JavaArray1D[] rangeTuples = new JavaArray1D[1];
    rangeTuples[0] = new JavaArray1D(range);

    return new MipMap(new JavaArray1D(domain), rangeTuples);
  }

  public void addListener(DatasetListener<Tuple2D> listener) {
    ArgChecker.isNotNull(listener, "listener");
    this.listeners.add(listener);
  }
  
  public void removeListener(DatasetListener<Tuple2D> listener) {
    listeners.remove(listener);
  }

  @Override
  public MipMapRegion getBestMipMapForInterval(Interval region, 
      int maxSamples) {

    region = region.copy();

    final double unitWidth = region.length() / maxSamples;

    Interval domainRegion = region.copy();
    region.intersect(domainExtrema);

    //Log.info("Graph wants region: " + region);

    int preferredUnitIdx = -1;
    for (int unitIdx = numUnits - 1; unitIdx >= 0; unitIdx--) {
      if (unitWidths[unitIdx] >= unitWidth) {
	preferredUnitIdx = unitIdx;
      }
    }

    if (preferredUnitIdx == -1) {
      // Pick the highest resolution if nothing else could be found.
      preferredUnitIdx = numUnits - 1;
    }

    if (intervalSets[preferredUnitIdx] == null) {
      intervalSets[preferredUnitIdx] = new IntervalSet();
    }
    IntervalSet unitIntervalSet = intervalSets[preferredUnitIdx];
    IntervalSet requestSet = new IntervalSet();
    requestSet.add(region);
    requestSet = requestSet.difference(unitIntervalSet);
    if (requestSet.size() != 0) {
      for (int i = 0; i < requestSet.size(); i++) {
	Interval requestRegion = requestSet.get(i);
	unitIntervalSet.add(requestRegion);
	Log.info("Request " + requestRegion + " for unitIdx=" 
            + preferredUnitIdx);
	model.requestData(new Request(requestRegion, preferredUnitIdx), 
	    new DelegateCallback(preferredUnitIdx));
      }
    }

    int domainStartIdx;
    int domainEndIdx;

    // FIXME: maybe not just get the first, but get the best matching?
    // Maybe its better to get a low-res mip-map with many 
    for (int unitIdx = preferredUnitIdx; unitIdx < numUnits; unitIdx++) {
      MipMap bestMipMap = mipMaps[unitIdx];
      if (bestMipMap != null) {
	Array1D domain = bestMipMap.getDomain();
	domainStartIdx = Util.binarySearch(domain, domainRegion.getStart());
	domainEndIdx = Util.binarySearch(domain, domainRegion.getEnd());
	return new MipMapRegion(bestMipMap, domainStartIdx, domainEndIdx);
      }
    }

    Array1D domain = extremaMipMap.getDomain();
    domainStartIdx = Util.binarySearch(domain, domainRegion.getStart());
    domainEndIdx = Util.binarySearch(domain, domainRegion.getEnd());
    return new MipMapRegion(extremaMipMap, domainStartIdx, domainEndIdx);
  }

  private void validateDomain(double[] domain) {
    for (int i = 0; i < domain.length - 1; i++) {
      if (domain[i] == domain[i + 1])
	Log.info("bad domain: idx: " + i);
    }
  }

  public void provideData(int unitIdx, double[] domain, double[] range) {
    ArgChecker.isNotNull(domain, "domain");
    ArgChecker.isNotNull(range, "range");

    if (domain.length == 0)
      return;
    validateDomain(domain);

    MipMap mipMap = mipMaps[unitIdx];
    if (mipMap == null) {
      mipMaps[unitIdx] = mipMap = createExtremaMipMap(domainExtrema);

      for (int i = unitIdx - 1; i >= 0; i--) {
	if (mipMaps[i] != null) {
	  mipMaps[i].nextMipMap = mipMap;
	  break;
	}
      }
      
      if (unitIdx == 0)
	extremaMipMap.nextMipMap = mipMap;
    } 
      
    Array1D domainArray = mipMap.getDomain();
    Interval providedDomain = new Interval(domain[0],
        domain[domain.length - 1]);

    int startIdx = Util.binarySearch(domainArray, 
        providedDomain.getStart());
    int stopIdx = Util.binarySearch(domainArray, 
        providedDomain.getEnd());

    if (domainArray.get(startIdx) >= providedDomain.getStart()) {
      startIdx = Math.max(0, startIdx - 1);
    }
    if (domainArray.get(stopIdx) <= providedDomain.getEnd()) {
      stopIdx++;
    }
    
    double[] newDomainArray = mergeArrays(domainArray.backingArray(),
        domainArray.size(), domain, startIdx, stopIdx);

    Array1D rangeArray = mipMap.getRange(0);
    double[] newRangeArray = mergeArrays(rangeArray.backingArray(),
	rangeArray.size(), range, startIdx, stopIdx);

//     Log.info("create new: "
// 	     + "; newDomainArray.length=" + newDomainArray.length
// 	     + "; newRangeArray.length=" + newRangeArray.length);

    mipMaps[unitIdx] = new MipMap(new JavaArray1D(newDomainArray), 
        new JavaArray1D(newRangeArray));

    notifyListeners(this, providedDomain.getStart(), 
        providedDomain.getEnd());
  }

  private double[] mergeArrays(double[] original, int origSize,
      double[] newData, int origStartIdx, int origEndIdx) {

    int numSamples = origStartIdx + newData.length 
      + (origSize - origEndIdx);
    double[] newArray = new double[numSamples];

    if (numSamples == newData.length) {
      System.arraycopy(newData, 0, newArray, 0, newData.length);
      return newArray;
    }

    int pos = 0;
    System.arraycopy(original, 0, newArray, pos, origStartIdx);
    pos += origStartIdx;
    System.arraycopy(newData, 0, newArray, pos, newData.length);
    pos += newData.length;
    System.arraycopy(original, origEndIdx, newArray, pos, 
        origSize - origEndIdx);

    return newArray;
  }

  private void notifyListeners(Dataset<Tuple2D> ds, double domainStart, double domainEnd) {
    for (DatasetListener<Tuple2D> l : this.listeners) {
      l.onDatasetChanged(ds, domainStart, domainEnd);
    }
  }

  public final Interval getDomainExtrema() {
    return this.domainExtrema;
  }

  public MipMapChain getMipMapChain() {
    List<MipMap> mipMaps = new ArrayList<MipMap>();
    MipMap mipMap = extremaMipMap;
    while (mipMap != null) {
      mipMaps.add(mipMap);
      mipMap = mipMap.next();
    }
    return new MipMapChain(mipMaps, 1);
  }
  
}