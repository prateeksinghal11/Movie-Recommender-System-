package com.recommender;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.svd.ALSWRFactorizer;
import org.apache.mahout.cf.taste.impl.recommender.svd.SVDRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

public class MatrixFactorizationRecommender {

	public static void main(String[] args) throws IOException, TasteException {
		


		// Code to Predict top 3 Movies
		DataModel model = new FileDataModel(new File(
				System.getProperty("user.dir")
						+ "/data/uaBaseTraining and Testing Merged.csv"));// File
		// name
		// and
		// path
		// to
		// be
		// changed(File->
		// On
		// which
		// predictions
		// are
		// to
		// be
		// made)

		UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
		
		
		
	
		SVDRecommender recommender =new SVDRecommender(model, new ALSWRFactorizer(model, 20, 0.05, 5));

		// int i = 1;

		ArrayList<String> arrayList = new ArrayList<String>();
		double total = 0;
		double error = 0;

		for (int i = 1; i <= 943; i++) {

			String rec = i + ",";

			List<RecommendedItem> recommendations = recommender.recommend(i, 3);

			for (RecommendedItem recommendation : recommendations) {

				rec += recommendation.getItemID() + ","
						+ recommendation.getValue() + ",";

			}
			if (rec.charAt(rec.length() - 1) == ',') {
				rec = (String) rec.subSequence(0, rec.length() - 1);
			}

			rec += "\n";
			arrayList.add(rec);
			System.out.println(rec);

		}

		PrintWriter printWriter = new PrintWriter(
				System.getProperty("user.dir")
						+ "\\Top3PredictedMoviesUsingMatrixFactorization.csv");// Gives
		// the
		// output
		// of
		// the
		// files
		printWriter
				.append("User ID, Movie1, Movie1 Predicted Rating, Movie2, Movie2 Predicted Rating, Movie3, Movie3 Predicted Rating\n");
		for (String userRating : arrayList) {
			printWriter.append(userRating);
		}

		printWriter.close();
		System.out
				.println("\n\nPlease Refer file for Top 3 predictions for each user:: "
						+ System.getProperty("user.dir")
						+ "\\Top3PredictedMoviesUsingMatrixFactorization.csv");
	
	}

}
