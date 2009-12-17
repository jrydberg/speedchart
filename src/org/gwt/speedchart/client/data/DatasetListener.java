package org.gwt.speedchart.client.data;

import org.gwt.speedchart.client.Dataset;
import org.gwt.speedchart.client.data.tuple.Tuple2D;

/**
 * Tracks modifications to an {@link Datasets} container and its
 * constituent {@link Dataset} elements.
 */
public interface DatasetListener<T extends Tuple2D> {

  /**
   * When an XYDataset is modified, this method is invoked with an interval
   * which bounds the span of domain encompassing all the changes that took
   * place.
   */
  void onDatasetChanged(Dataset<T> dataset, double domainStart, double domainEnd);
  
  /**
   * Fired when a dataset is removed from this container.
   * 
   * @param dataset - The dataset that was just removed.
   * @param datasetIndex - the ordinal position of the dataset that
   *    was just removed.  
   */
  void onDatasetRemoved(Dataset<T> dataset, int datasetIndex);
  
  /**
   * The dataset that was just added to this container.
   */
  void onDatasetAdded(Dataset<T> dataset);
  
}
