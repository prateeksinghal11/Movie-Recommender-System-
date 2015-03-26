setwd("D:/webanalytics/MovieLens-workshop/100Kfile/ml-100k")

library(recommenderlab)
library(reshape2)
##Load merged dataset



merged_file<-read.csv("uaBaseTraining and Testing Merged.csv",header=F)
colnames(merged_file)<-c("userId","movieId","rating")
full_cast<-dcast(merged_file,userId~movieId)

full_cast<-as.matrix(full_cast)
train <- as((full_cast), "realRatingMatrix")
#View(train)
#train_normalized<-normalize(train)
#image(train, main = "Raw Ratings")
#image(train_normalized, main = "Normalized Ratings")

seed<-1234
set.seed(seed)
train_set <- evaluationScheme(train, method="split", train=0.9,given=5,goodRating=3)

### User Based Collaborative Filtering -Jaccard Distance

recomend_ubcf <- Recommender(getData(train_set, "train"), method = "UBCF",param=list(method="Jaccard", minRating=1))

pred_ubcf_1 <- predict(recomend_ubcf, getData(train_set, "known"), type="ratings")

error_ubcf<-calcPredictionAccuracy(pred_ubcf_1, getData(train_set, "unknown"))

### User Based Collaborative Filtering -Cosine Distance

recomend_ubcf_cosine <- Recommender(getData(train_set, "train"), method = "UBCF",param=list(method="Cosine", minRating=1))

pred_ubcf_1_cosine <- predict(recomend_ubcf_cosine, getData(train_set, "known"), type="ratings")

error_ubcf_cosine<-calcPredictionAccuracy(pred_ubcf_1_cosine, getData(train_set, "unknown"))

### Item Based Collaborative Filtering -Jaccard Distance

recomend_ibcf <- Recommender(getData(train_set, "train"), method = "IBCF",param=list(method="Jaccard", minRating=1))

pred_ibcf_1 <- predict(recomend_ibcf, getData(train_set, "known"), type="ratings")

error_ibcf<-calcPredictionAccuracy(pred_ibcf_1, getData(train_set, "unknown"))

### Item Based Collaborative Filtering -Cosine Distance

recomend_ibcf_cosine <- Recommender(getData(train_set, "train"), method = "IBCF",param=list(method="Cosine", minRating=1))

pred_ibcf_1_cosine <- predict(recomend_ibcf_cosine, getData(train_set, "known"), type="ratings")

error_ibcf_cosine<-calcPredictionAccuracy(pred_ibcf_1_cosine, getData(train_set, "unknown"))

error<-rbind(error_ubcf,error_ubcf_cosine,error_ibcf,error_ibcf_cosine)
rownames(error) <- c("UBCF_Jaccard","UBCF_Cosine","IBCF_Jaccard","IBCF_Cosine")
error


### IBCF with Cosine distance measire gives the least error. Using IBCF to predict topN movies
reco_set <- as((full_cast), "realRatingMatrix")
r <- Recommender(reco_set, method = "IBCF",param=list(method="Cosine", minRating=1))
recom <- predict(r, 10:12 ,data=reco_set, n=3)
as(recom,"list") 

#### In the code snippet above, we supply a list of users in the predict and we get the top three 
#### predictions in recom object which can be viewed as a list.



