package com.recommender;

import java.io.File;
import java.io.IOException;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.IRStatistics;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.eval.RecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.eval.AverageAbsoluteDifferenceRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.GenericRecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.eval.RMSRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

public class RecommenderEvaluationTestCase2WithKNN {

	
	// Run this file for Test Case 2 
	
	public static void main(String[] args) throws IOException, TasteException {
		// TODO Auto-generated method stub
		DataModel model = new FileDataModel(new File(System.getProperty("user.dir")+"/data/uaBaseTraining and Testing Merged.csv"));
		RecommenderEvaluator evaluator = new RMSRecommenderEvaluator();
		RecommenderEvaluator evaluatoravg = new AverageAbsoluteDifferenceRecommenderEvaluator();
		RecommenderBuilder builder = new MyRecommenderBuilder();
		
		
		double resultavg = evaluatoravg.evaluate(builder, null, model, 0.7, 1.0);
		System.out.println("Avg Absolute Error: "+resultavg);
		
		
		double result = evaluator.evaluate(builder, null, model, 0.7, 1.0);
		System.out.println("Root Mean Square Error: "+result);
		//RecommenderIRStatsEvaluator irevaluator = new GenericRecommenderIRStatsEvaluator();
		/*IRStatistics irStatistics = irevaluator.evaluate
				(builder, null,
						model, null, 
						5,GenericRecommenderIRStatsEvaluator.CHOOSE_THRESHOLD,1.0);
		*/
		//System.out.println("Precision: "+irStatistics.getPrecision()+"\nRecall: "+irStatistics.getRecall());
	}

}

class MyRecommenderBuilder implements RecommenderBuilder
{

	public Recommender buildRecommender(DataModel dataModel) throws TasteException
	{
		UserSimilarity similarity = new PearsonCorrelationSimilarity(dataModel);
		UserNeighborhood neighborhood = new NearestNUserNeighborhood(200, similarity, dataModel);
		return new GenericUserBasedRecommender(dataModel, neighborhood, similarity);	
	}
}