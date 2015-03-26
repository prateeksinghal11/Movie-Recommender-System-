package com.recommender;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

public class Top3PredictionsWithKNN {

	// Run this file for Top 3 Predictions
	
	public static void main(String[] args) throws TasteException, IOException {

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

		UserNeighborhood neighborhood = new NearestNUserNeighborhood(200,
				similarity, model);
		UserBasedRecommender recommender = new GenericUserBasedRecommender(
				model, neighborhood, similarity);

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
						+ "\\Top3PredictedMovies.csv");// Gives
		// the
		// output
		// pf
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
						+ "\\Top3PredictedMovies.csv");
	}

}
