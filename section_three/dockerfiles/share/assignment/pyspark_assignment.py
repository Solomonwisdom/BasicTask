from pyspark.sql import SparkSession
from pyspark.sql import functions


if __name__ == "__main__":
    spark = SparkSession \
        .builder \
        .appName("PysparkAssignment") \
        .getOrCreate()
    bank_data = spark.read.csv("input/bank-data.csv", header=True)
    print(bank_data.groupBy('sex').agg(functions.max(bank_data.income), functions.avg(bank_data.income)).collect())
    print(bank_data.groupBy('region').agg(functions.avg(bank_data.income)).collect())
    x = input()


