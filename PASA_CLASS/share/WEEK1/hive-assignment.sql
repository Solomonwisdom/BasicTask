# create table
CREATE TABLE CSVTable(id STRING, age INT, sex STRING, 
    region STRING, income DOUBLE, married STRING, 
    children INT, car STRING, save_act STRING, current_act STRING, 
    mortgage STRING, pep STRING) 
    ROW FORMAT DELIMITED FIELDS TERMINATED BY ',' LINES TERMINATED BY '\n'
    tblproperties( 
        "skip.header.line.count"="1"
    );
# load data
LOAD DATA LOCAL INPATH '/root/experiment/bank-data.csv' INTO TABLE CSVTable;
# query
SELECT region,AVG(income) FROM CSVTable WHERE age>30 and sex='MALE' GROUP BY region;
