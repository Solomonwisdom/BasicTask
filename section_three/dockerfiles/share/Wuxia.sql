# create table
CREATE TABLE Wuxia(word STRING, count DOUBLE) row format delimited fields terminated by '\t' stored as textfile;
# load data
LOAD DATA LOCAL INPATH '/root/experiment/count.txt' INTO TABLE Wuxia;
# query
SELECT `word` FROM Wuxia WHERE `count`>300;
SELECT `word`,`count` FROM Wuxia WHERE `count`>300 ORDER BY `count` DESC LIMIT 100;
