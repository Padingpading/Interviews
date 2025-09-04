//
//
//package com.padingpading.spring.cacheable;
//
//import cn.hutool.core.collection.CollUtil;
//import com.zaxxer.hikari.HikariDataSource;
//import org.springframework.beans.factory.InitializingBean;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.cache.annotation.CacheEvict;
//import org.springframework.cache.annotation.CachePut;
//import org.springframework.cache.annotation.Cacheable;
//import org.springframework.core.env.Environment;
//import org.springframework.jdbc.core.BeanPropertyRowMapper;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.core.RowMapper;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//
///**
// * https://blog.csdn.net/qq_32088869/article/details/130240851
// */
//@Service
//public class CacheAbleServiceImpl implements CacheAbleService, InitializingBean {
//
//    @Autowired
//    private JdbcTemplate jdbcTemplate;
//
//    @Value("${spring.datasource.url}")
//    private String springDatasourceUrl;
//
//    @Value("${spring.datasource.username}")
//    private String springUserName;
//
//    @Value("${spring.datasource.password}")
//    private String springUserPassword;
//
//    @Value("${spring.datasource.driver-class-name}")
//    private String springDatasourceDriver;
//
//    @Autowired
//    private Environment environment;
//
//
//    @Override
//    public Boolean add(CacheAbleDo cacheAbleDo) {
//        String sql = "INSERT INTO spring_cache_able (name, age) VALUES (?, ?)";
//        int update = jdbcTemplate.update(sql,"ls",1);
//        return update>0;
//    }
//
//    @Override
//    @CachePut(cacheNames = "cacheable", key = "#cacheAbleDo.id",unless="#result == null") //清空某一个key的缓存
//    public CacheAbleDo update(CacheAbleDo cacheAbleDo) {
//        String sql = "UPDATE spring_cache_able SET age = ?,name = ? WHERE id = ?;";
//        int update = jdbcTemplate.update(sql,cacheAbleDo.getAge(),cacheAbleDo.getName(),cacheAbleDo.getId());
//        if(update>0){
//            return cacheAbleDo;
//        }
//        return null;
//    }
//
//    @Override
//    @CacheEvict(cacheNames = "cacheable", key = "#cacheAbleDo.id") //清空某一个key的缓存
//    public Boolean delete(CacheAbleDo cacheAbleDo) {
//        String sql = "UPDATE spring_cache_able SET status = 1   WHERE id = ?;";
//        int update = jdbcTemplate.update(sql,cacheAbleDo.getId());
//        return update> 0 ;
//    }
//
//    @Override
//    @Cacheable(value = "cacheable",key ="#id",unless="#result == null",condition = "#id==1")
//    public CacheAbleDo select(Integer id) {
//        String sql = "SELECT id,name,age FROM spring_cache_able where id="+id;
//        RowMapper<CacheAbleDo> rowMapper1=new BeanPropertyRowMapper<CacheAbleDo>(CacheAbleDo.class);
//        List<CacheAbleDo> query = jdbcTemplate.query(sql, rowMapper1);
//        if(CollUtil.isNotEmpty(query)){
//            return query.get(0);
//        }
//        return null;
//    }
//
//    @Override
//    @Cacheable(value = "cacheable",key ="#t.name")
//    public List<CacheAbleDo> selectByParam(CacheAbleDo t) {
//        String sql = "SELECT id,name,age FROM spring_cache_able where name='" + t.getName() +"' and age='" + t.getAge()+"'";
//        RowMapper   <CacheAbleDo> rowMapper1=new BeanPropertyRowMapper<CacheAbleDo>(CacheAbleDo.class);
//        List<CacheAbleDo> query = jdbcTemplate.query(sql, rowMapper1);
//        return query;
//    }
//
//
//    @Override
//    public void afterPropertiesSet() throws Exception {
//        HikariDataSource dataSource = new HikariDataSource();
//        dataSource.setJdbcUrl(springDatasourceUrl);
//        dataSource.setUsername(springUserName);
//        dataSource.setPassword(springUserPassword);
//        dataSource.setDriverClassName(springDatasourceDriver);
//        this.jdbcTemplate = new JdbcTemplate(dataSource);
//        //创建表语句
//        String sql = "CREATE TABLE IF NOT EXISTS spring_cache_able\n" + "(\n"
//                + "    id   bigint auto_increment comment '主键'\n" + "        primary key,\n"
//                + "    name varchar(64) default '' not null comment '昵称',\n"
//                + "    age  bigint      default 0  not null comment '年龄',\n"
//                + "    status  bigint      default 0  not null comment '状态'\n" + ")\n"
//                + "    comment 'spring的Cache_able注解' collate = utf8mb4_unicode_ci;";
//        jdbcTemplate.execute(sql);
//    }
//}
