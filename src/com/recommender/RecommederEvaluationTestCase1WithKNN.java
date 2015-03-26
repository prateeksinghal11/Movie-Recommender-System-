package com.recommender;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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

public class RecommederEvaluationTestCase1WithKNN {

	//Run this file for Test Case 1 
	
	public static void main(String[] args) throws IOException, TasteException {
		// Code for Testing the Predictions

		DataModel model = new FileDataModel(new File(
				System.getProperty("user.dir")+"/data/uabase Training.csv"));

		// System.out.println("Number of Unique Users:" + model.getNumUsers());
		// System.out.println("Number of Unique Movies:" + model.getNumItems());

		UserSimilarity similarity = new PearsonCorrelationSimilarity(model);

		UserNeighborhood neighborhood = new NearestNUserNeighborhood(200,
				similarity, model);
		UserBasedRecommender recommender = new GenericUserBasedRecommender(
				model, neighborhood, similarity);

		ArrayList<UserRating> userRatings = getTest();
		System.out.println(userRatings.size());
		int i = 1;

		PrintWriter printWriter = new PrintWriter(System.getProperty("user.dir")+"\\TestingResults.csv");
		ArrayList<String> arrayList = new ArrayList<String>();
		double total = 0;
		double error = 0;

		int[][] confusionMatrix = new int[][] { { 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0 }

		};

		for (UserRating userRating : userRatings) {

			String rec = i + ",";

			List<RecommendedItem> recommendations = recommender.recommend(i,
					2000);

			for (RecommendedItem recommendation : recommendations) {

				for (Rating mov : userRating.ratingMovieList) {

					if (mov.movie == recommendation.getItemID()) {
						String str = userRating.userID + ","
								+ recommendation.getItemID() + ","
								+  recommendation.getValue()
								+ "," + mov.rating;
						arrayList.add(str);

						double act = mov.rating;
						double test = recommendation.getValue();

						double diff = Math.abs(act - test);

						error += diff;

						int actual = (int) Math
								.round(recommendation.getValue());
						int pred = (int) mov.rating;

						confusionMatrix[actual - 1][pred - 1]++;

						System.out.println("User ID :" + userRating.userID
								+ " Item:" + recommendation.getItemID()
								+ " Predicted:"
								+ recommendation.getValue()
								+ " Actual:" + mov.rating);
						// System.out.println(recommendation);
						total++;
						break;
					}
				}

			}
			if (rec.charAt(rec.length() - 1) == ',') {
				rec = (String) rec.subSequence(0, rec.length() - 1);
			}

		
			i++;
		}
		
		System.out.println("Error = " + error);
		System.out.println("Total =" + total);

		double d = Double.parseDouble(String.valueOf(error))
				/ Double.parseDouble(String.valueOf(total));
		

		System.out.println("Avg. Mean Absolute Error = "
				+ ((float) error / total));
		printWriter.append("User ID, Movie, Predicted Rating, Actual Rating\n");		
		for (String userRating : arrayList) {
			printWriter.append(userRating + "\n");
		}
		// out.println(Rec);
		printWriter.close();
		System.out.println("\t\tConfusion Matrix");
		
		System.out.println("          Predicted(1) Preducted(2) Predicted(3) Predicted(4) Predicted(5)");
		for (int k = 0; k < 5; k++) {
			
			System.out.print("Actual("+(k+1)+")  ");
			for (int j = 0; j < 5; j++) {
				System.out.print(confusionMatrix[k][j]+"\t\t");				
			}
			System.out.println();
		}
		
		
		System.out.println("\n\nPlease Refer file for predicted ratings on the test dataset :: "+System.getProperty("user.dir")+"\\TestingResults.csv");
	}

	public static ArrayList<UserRating> getTest() throws NumberFormatException,
			IOException {

		ArrayList<UserRating> arrayList = new ArrayList<UserRating>();
		for (int i = 1; i <= 943; i++) {
			String csvFile = System.getProperty("user.dir")+"/data/uatest Testing.csv";
			BufferedReader br = null;
			BufferedReader brDemo = null;
			String line = "";
			String cvsSplitBy = ",";

			br = new BufferedReader(new FileReader(csvFile));

			UserRating user = new UserRating();
			user.userID = i;
			while ((line = br.readLine()) != null) {
				String[] cell = line.split(",");
				// System.out.println(cell[0]);

				if (Integer.parseInt(cell[0]) == (i)) {
					// System.out.println(cell[0]+"  "+i);
					Rating rating = new Rating();
					rating.movie = Integer.parseInt(cell[1]);
					rating.rating = Integer.parseInt(cell[2]);
					// System.out.println(i);
					user.ratingMovieList.add(rating);
					// System.out.println(line);
				}
			}
			arrayList.add(user);
		}
		return arrayList;
	}
}

class Rating {
	int movie;
	double rating;
}

class UserRating {
	int userID;
	ArrayList<Rating> ratingMovieList = new ArrayList<Rating>();
}