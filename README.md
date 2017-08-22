# Ratelimit-framework
A  framework that can throttle client requests in distributed system.
Ratelimiting can be applied across method, apis for a particular company.
This framework uses centralized Memcached to keep track of API hits.

# Steps to Use

1. Import module ratelimit in your project.

```

<dependency>
	<artifactId>ratelimit</artifactId>
	<groupId>com.phonepe</groupId>
	<version>0.0.1-SNAPSHOT</version>
</dependency>

```
    
2. Ensure the package com.phonepe is in your component scan path

3. Place memcache.properties file in your classpath and set memcache.servers.ip as the ip:port of memcache nodes.

4. Create RateLimit object and pass it to IRateLimitService.addRateLimit(String company, RateLimit rateLimit) method;

#Example RateLimit Object

```
RateLimit rateLimit = new RateLimit();

String company = "E-COM";

Limit limit = new Limit();

limit.getDurationMap().put(TimeUnit.HOUR, 100);
limit.getDurationMap().put(TimeUnit.WEEK, 200);
limit.getDurationMap().put(TimeUnit.MONTH, 10000);

rateLimit.setClientLimit(limit);

Limit limit2 = new Limit();

limit2.getDurationMap().put(TimeUnit.SECOND, 10);
limit2.getDurationMap().put(TimeUnit.MINUTE, 20);
limit2.getDurationMap().put(TimeUnit.WEEK, 700);

rateLimit.getLimits().put(company + "_GET", limit2);

rateLimit.getLimits().put(company + "_/pay", limit2);

Limit limit3 = new Limit();

limit3.getDurationMap().put(TimeUnit.SECOND, 20);
limit3.getDurationMap().put(TimeUnit.HOUR, 60);
limit3.getDurationMap().put(TimeUnit.WEEK, 900);
limit3.getDurationMap().put(TimeUnit.MONTH, 1000);

rateLimit.getLimits().put(company + "_POST", limit3);

rateLimit.getLimits().put(company + "_/status", limit3);

```


