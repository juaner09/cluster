package com.betty.similarity.naive;

import com.betty.model.Dataset;
import com.betty.model.Item;
import com.betty.recommender.RatingCountMatrix;


public class ImprovedItemBasedSimilarity extends SimilarityMatrixImpl {

    /**
	 * Unique identifier for serialization
	 */
	private static final long serialVersionUID = -8364129617679022295L;

	public ImprovedItemBasedSimilarity(String id, Dataset dataSet, boolean keepRatingCountMatrix) {
        this.id = id;
        this.keepRatingCountMatrix = keepRatingCountMatrix;
        this.useObjIdToIndexMapping = dataSet.isIdMappingRequired(); 
        calculate(dataSet);
    }
    
    @Override
	protected void calculate(Dataset dataSet) {
        int nItems = dataSet.getItemCount();
        int nRatingValues = 5;
        similarityValues = new double[nItems][nItems];
        
        if( keepRatingCountMatrix ) {
            ratingCountMatrix = new RatingCountMatrix[nItems][nItems];
        }
        
        // if we want to use mapping from itemId to index then generate 
        // index for every itemId
        if( useObjIdToIndexMapping ) {
            for(Item item : dataSet.getItems() ) {
                idMapping.getIndex(String.valueOf(item.getId()));
            }
        }
        
        int totalCount=0;
        int agreementCount=0;
        
        for (int u = 0; u < nItems; u++) {
            int itemAId = getObjIdFromIndex(u);
            Item itemA = dataSet.getItem(itemAId);
            // we only need to calculate elements above the main diagonal.
            for (int v = u + 1; v < nItems; v++) {
                int itemBId = getObjIdFromIndex(v);
                Item itemB = dataSet.getItem(itemBId);
                RatingCountMatrix rcm = new RatingCountMatrix(itemA, itemB, nRatingValues);
                
                totalCount = rcm.getTotalCount();
                agreementCount = rcm.getAgreementCount();
                
                if (agreementCount > 0) {
                    /*
                     * See ImprovedUserBasedSimilarity class for detailed
                     * explanation.
                     */
                    double weightedDisagreements = 0.0;
                    int maxBandId = rcm.getMatrix().length - 1;
                    for (int matrixBandId = 1; matrixBandId <= maxBandId; matrixBandId++) {
                        double bandWeight = matrixBandId;
                        weightedDisagreements += bandWeight
                                * rcm.getBandCount(matrixBandId);
                    }

                    double similarityValue = 1.0 - (weightedDisagreements / 
                            totalCount);

                    // normalizing to [0..1]
                    double normalizedSimilarityValue = (similarityValue - 1.0 + maxBandId)
                            / maxBandId;
                    similarityValues[u][v] = normalizedSimilarityValue;
                } else {
                    similarityValues[u][v] = 0.0;
                }

                // For large datasets
                if( keepRatingCountMatrix ) {
                    ratingCountMatrix[u][v] = rcm;
                }
            }

            // for u == v assign 1
            // ratingCountMatrix wasn't created for this case
            similarityValues[u][u] = 1.0;
        }
    }
}
