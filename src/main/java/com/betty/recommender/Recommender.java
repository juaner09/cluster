package com.betty.recommender;

import java.util.List;

import com.betty.model.Dataset;
import com.betty.model.Item;
import com.betty.model.SimilarUser;
import com.betty.model.User;

public interface Recommender {

    /**
     * Returns top 5 recommendations for the user.
     * 
     * @param user
     * @return recommended items with predicted ratings.
     */
    public List<PredictedItemRating> recommend(User user);
    
    /**
     * Returns top N recommendations for the user.
     * 
     * @param user 
     * @param topN number of top recommendations to return.
     * @return recommended items with predicted ratings.
     */
    public List<PredictedItemRating> recommend(User user, int topN);    
    
    // Predictions
    public double predictRating(User user, Item item);
    
    public double predictBasedOnItemAverage(Item item);
    
    public double predictBasedOnUserAverage(User user);
    
    // Similarities
    public SimilarUser[] findSimilarUsers(User user);
    
    public SimilarUser[] findSimilarUsers(User user, int topN);
    
    public SimilarItem[] findSimilarItems(Item item);    

    public SimilarItem[] findSimilarItems(Item item, int topN);    
    
    // Auxiliary
    public Dataset getDataset();
    
    public double getSimilarityThreshold();
    
    public void setSimilarityThreshold(double similarityThreshold);    
}
