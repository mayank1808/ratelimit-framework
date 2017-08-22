# Ratelimit-framework
A  framework that can throttle client requests in distributed system.
Ratelimiting can be applied across method, apis for a particular company.
This framework uses centralized Memcached to keep track of API hits.

# Steps to Use

1. Import module ratelimit in your project.
    
    <dependency>
			<artifactId>ratelimit</artifactId>
			<groupId>com.phonepe</groupId>
			<version>0.0.1-SNAPSHOT</version>
		</dependency>
    
2. Ensure the package com.phonepe is in your component scan path

3. Place memcache.properties file in your classpath and set memcache.servers.ip as the ip:port of memcache nodes.

4. Create RateLimit object and pass it to IRateLimitService.addRateLimit(String company, RateLimit rateLimit) method;
