#Configuration table
INSERT INTO configuration (kei,value) VALUES('produban.indexer.type','elasticsearch');
INSERT INTO configuration (kei,value) VALUES('produban.indexer.clusterName','elasticsearch');
INSERT INTO configuration (kei,value) VALUES('produban.indexer.nodes','192.168.85.135:9300');
INSERT INTO configuration (kei,value) VALUES('produban.indexer.indexName','akamai');
INSERT INTO configuration (kei,value) VALUES('produban.sparkstreaming.mode','local[2]');
INSERT INTO configuration (kei,value) VALUES('produban.sparkstreaming.port','15000');
INSERT INTO configuration (kei,value) VALUES('produban.sparkstreaming.checkpoint','elasticsearch');
INSERT INTO configuration (kei,value) VALUES('produban.zookeeper.nodes','192.168.85.135:2181');
INSERT INTO configuration (kei,value) VALUES('produban.mongo.clusterName','192.168.85.135:27017');
INSERT INTO configuration (kei,value) VALUES('produban.mongo.db','mongo');

#Rules table
INSERT INTO rules (regex,numberTimes,window,slideWindow,message) VALUES('SQL',2,15000,5000,'SQL Injection detected');
INSERT INTO rules (regex,numberTimes,window,slideWindow,message) VALUES('BURST,SUMMARY',2,15000,5000,'DoS detected');
