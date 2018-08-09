1,创建用户基础信息表。
create external table if not exists ods_user_base_info_new(
udi String,
user_type string,
gender string,
prov_id String,
city_id String,
reg_time string,
user_status string,
cert_info string,
tag string,
birthday string,
dt string)
row format delimited fields terminated by '\t'
stored as textfile
location '/user/hive/warehouse/wb.db/ods_user_base_info_new';

例： SELECT  count(1) FROM ods_user_base_info_new WHERE  user_status='1' ;

2，行为日志表。
CREATE EXTERNAL TABLE if not exists ods_bhv_publog
(uid String,mid string ,
opp_uid string,
is_transmit String,
rootmid string,
rtmid string,
rootudi string)
partitioned by (dt string)
row format delimited fields terminated by '\001'
stored as TEXTFILE

为表增加分区，指定表位置
alter table ods_bhv_comment add partition(dt='20160516')
location '/user/hive/warehouse/wb.db/ods_bhv_publog/dt=20160516';

用户运动记录
create external table if not exists ods_prod_sport_record
(day_key string,
uid string,
key string,
record string,
mtime string
)
partitioned by (dt string)
row format delimiterd fields terminated by '\001'
stored as textfile
alter table ods_prod_sport_record add if not exists  partition(dt='20160522') location '/user/lyd/ods_prod_sport_record';

样本：2016-05-19 20:59:04	3266713293	146364480048141a37baf94514e7f4768679375994	workout_type=>walking,start_date=>1463644800,end_date=>1463648400,duration=>3600,distance=>777.56,energy_burned=>34.42,step_count=>1148	1463662744	20160519	NULL	NULL	NULL	NULL	20160516
天，周，总步数，公里数，卡路里数
/解析log 日志，---- 封装 成kv 设置常量  mr  ----hdfs  ，