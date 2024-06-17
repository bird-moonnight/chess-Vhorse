# 基本表

```sql
/创建数据库/ drop database if exists CommunityService; create database CommunityService;

/进入对应数据库/ use CommunityService;
-- 用户表--
CREATE TABLE user ( user_id INT NOT NULL AUTO_INCREMENT, user_name VARCHAR(20) NOT NULL, user_password VARCHAR(20) NOT NULL, user_phone VARCHAR(11) NOT NULL, user_address VARCHAR(100) NOT NULL, user_role ENUM('admin','employer','employee') NOT NULL, PRIMARY KEY (user_id) );

-- 服务类别表--
CREATE TABLE service ( service_id INT NOT NULL AUTO_INCREMENT, service_name VARCHAR(20) NOT NULL, service_description VARCHAR(200) NOT NULL, service_price DECIMAL(10,2) NOT NULL, service_duration INT NOT NULL, PRIMARY KEY (service_id) );

-- 订单表--
CREATE TABLE order ( order_id INT NOT NULL AUTO_INCREMENT, user_id INT NOT NULL, service_id INT NOT NULL, order_status ENUM('pending','confirmed','cancelled','completed') NOT NULL, order_amount DECIMAL(10,2) NOT NULL, order_time DATETIME NOT NULL, PRIMARY KEY (order_id), FOREIGN KEY (user_id) REFERENCES user(user_id), FOREIGN KEY (service_id) REFERENCES service(service_id) );

-- 订单评价表-- 
CREATE TABLE review ( review_id INT NOT NULL AUTO_INCREMENT, order_id INT NOT NULL, user_id INT NOT NULL, review_content VARCHAR(200) NOT NULL, review_score INT NOT NULL CHECK (review_score BETWEEN 1 AND 5), PRIMARY KEY (review_id), FOREIGN KEY (order_id) REFERENCES order(order_id), FOREIGN KEY (user_id) REFERENCES user(user_id) );

-- 工作人员表--
CREATE TABLE worker ( worker_id INT NOT NULL AUTO_INCREMENT, worker_name VARCHAR(20) NOT NULL, worker_gender ENUM('male','female') NOT NULL, worker_age INT NOT NULL CHECK (worker_age BETWEEN 18 AND 60), worker_address VARCHAR(100) NOT NULL, worker_phone VARCHAR(11) NOT NULL, service_id INT NOT NULL, PRIMARY KEY (worker_id), FOREIGN KEY (service_id) REFERENCES service(service_id) );

-- 订单分派表--
CREATE TABLE assignment ( assignment_id INT NOT NULL AUTO_INCREMENT, order_id INT NOT NULL, worker_id INT NOT NULL, PRIMARY KEY (assignment_id), FOREIGN KEY (order_id) REFERENCES order(order_id), FOREIGN KEY (worker_id) REFERENCES worker(worker_id) );
```

# 视图

```sql
-- 创建订单详情视图--
CREATE VIEW order_detail AS SELECT o.order_id, u.user_name, s.service_name, w.worker_name, o.order_status, o.order_amount, o.order_time FROM order o JOIN user u ON o.user_id = u.user_id JOIN service s ON o.service_id = s.service_id JOIN assignment a ON o.order_id = a.order_id JOIN worker w ON a.worker_id = w.worker_id;

-- 创建服务类别统计视图：来显示每个服务类别的订单数量和总金额--
CREATE VIEW service_stats AS SELECT s.service_name, COUNT(o.order_id) AS order_count, SUM(o.order_amount) AS order_amount FROM service s LEFT JOIN order o ON s.service_id = o.service_id GROUP BY s.service_name;

-- 创建用户统计视图：来显示每个用户的订单数量和平均评分--
CREATE VIEW user_stats AS SELECT u.user_name, COUNT(o.order_id) AS order_count, AVG(r.review_score) AS avg_score FROM user u LEFT JOIN order o ON u.user_id = o.user_id LEFT JOIN review r ON o.order_id = r.order_id GROUP BY u.user_name;

-- 创建工作人员统计视图：来显示每个工作人员的订单数量和平均工资--
CREATE VIEW worker_stats AS SELECT w.worker_name, COUNT(a.assignment_id) AS assignment_count, AVG(s.service_price * s.service_duration) AS avg_salary FROM worker w LEFT JOIN assignment a ON w.worker_id = a.worker_id LEFT JOIN order o ON a.order_id = o.order_id LEFT JOIN service s ON w.service_id = s.service_id GROUP BY w.worker_name;
```



# 注册/登录

```sql
-- 注册-- 
INSERT INTO user (user_name, user_password, user_phone, user_address, user_role) VALUES ('张三', '123456', '13888888888', '北京市朝阳区', 'employer');

-- 登录--
SELECT * FROM user WHERE user_name = '张三' AND user_password = '123456';
```

