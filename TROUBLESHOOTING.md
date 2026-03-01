# 🐛 Troubleshooting Guide

Common issues and their solutions when deploying the Restaurant Platform.

## Backend Issues

### Issue: Database Connection Failed

**Error Message:**
```
org.postgresql.util.PSQLException: Connection refused
java.sql.SQLException: Unable to get a connection
```

**Causes:**
- Supabase credentials are incorrect
- Database host/port is wrong
- Firewall blocking connection
- Supabase database not ready

**Solutions:**

1. **Verify Credentials**:
   ```bash
   # Check environment variables in Render dashboard
   # Ensure format: jdbc:postgresql://HOST:5432/postgres
   ```

2. **Test Connection Locally**:
   ```bash
   psql -h [SUPABASE_HOST] -U postgres -d postgres
   # Enter password when prompted
   ```

3. **Check Firewall**:
   - Supabase allows connections by default from any IP
   - No additional firewall rules needed

4. **Verify Supabase**:
   - Dashboard → Settings → Database
   - Ensure project is "Active"
   - Check database status is "Running"

---

### Issue: Flyway Migration Failed

**Error Message:**
```
org.flywaydb.core.internal.command.DbValidate$ValidateResult
ERROR: Validate failed
```

**Common Causes:**
- Incorrect SQL syntax
- Wrong file naming (not V#__description.sql)
- Duplicate version numbers
- Foreign key constraints failed

**Solutions:**

1. **Check File Naming**:
   ```bash
   # Should be:
   V1__create_user_table.sql        ✅ (double underscore)
   V2__create_restaurant_table.sql  ✅
   V1_create_user_table.sql         ❌ (single underscore)
   ```

2. **Validate SQL Syntax**:
   ```bash
   # Test locally
   psql -h localhost -U postgres -f src/main/resources/db/migration/V1__create_user_table.sql
   ```

3. **Check for Duplicates**:
   ```bash
   ls -la src/main/resources/db/migration/ | grep V[0-9]
   # Should see only one file per version
   ```

4. **For Existing Databases**:
   ```properties
   # In application.properties
   spring.flyway.baseline-on-migrate=true
   spring.flyway.baseline-version=0
   ```

5. **Reset Migrations (⚠️ Data Loss)**:
   ```sql
   -- In Supabase SQL editor
   DROP TABLE IF EXISTS flyway_schema_history;
   ```

---

### Issue: Actuator Health Check Fails

**Error:**
```
404 Not Found at /actuator/health
```

**Causes:**
- Actuator not installed
- Actuator endpoints not exposed
- Wrong path (should be /api/actuator/health)

**Solutions:**

1. **Verify Actuator Dependency**:
   ```xml
   <!-- pom.xml should have: -->
   <dependency>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-actuator</artifactId>
   </dependency>
   ```

2. **Check Configuration**:
   ```properties
   # application.properties
   management.endpoints.web.exposure.include=health,info
   management.endpoint.health.show-details=always
   ```

3. **Test Endpoint**:
   ```bash
   # Locally
   curl http://localhost:8080/api/actuator/health
   
   # Render
   curl https://[your-service].onrender.com/api/actuator/health
   ```

---

### Issue: Render Build Timeout

**Error:**
```
Build exceeded maximum duration
```

**Causes:**
- Large dependency downloads
- Slow database migrations
- Free tier resource limitations

**Solutions:**

1. **Upgrade Plan**:
   - Free tier has 30-min build limit
   - Paid tier has 1-hour limit
   - Consider upgrade for production

2. **Optimize Build**:
   ```bash
   # Use skiptests
   mvn clean package -DskipTests
   
   # In Dockerfile
   RUN ./mvnw clean package -DskipTests --quiet
   ```

3. **Check Logs**:
   - Render Dashboard → Logs
   - Look for slow steps
   - Cache dependencies if possible

---

## Frontend Issues

### Issue: Frontend Shows Blank Page or 404

**Symptoms:**
- Vercel shows "404 Not Found"
- Page loads but no content
- White screen with errors in console

**Causes:**
- Wrong build output directory
- Angular build failed
- Routing not configured properly

**Solutions:**

1. **Check Build Output Directory**:
   ```bash
   # Local build
   npm run build
   
   # Verify dist/frontend exists
   ls -la dist/
   ```

2. **Verify Vercel Configuration**:
   - Settings → Build & Deployment
   - Build Command: `npm run build`
   - Output Directory: `dist/frontend`
   - Root Directory: `frontend` ✅

3. **Check Build Logs**:
   - Vercel Dashboard → Deployments
   - Click latest deployment
   - Check logs for errors

4. **Test Locally**:
   ```bash
   npm run build
   npm install -g http-server
   http-server dist/frontend
   # Visit http://localhost:8080
   ```

---

### Issue: CORS Errors

**Error Message:**
```
Access to XMLHttpRequest from origin 'https://restaurant-platform.com'
has been blocked by CORS policy
```

**In Browser Console:**
```
CORS policy: Response to preflight request doesn't pass access control check
```

**Causes:**
- Backend CORS not configured
- Frontend URL not in allowed origins
- Credentials mismatch

**Solutions:**

1. **Update CORS Origins**:
   ```bash
   # In Render Dashboard → Environment
   CORS_ALLOWED_ORIGINS=https://restaurant-platform.com
   ```

2. **Restart Backend**:
   ```bash
   # Render automatically restarts with new env vars
   # Force restart: Settings → Restart service
   ```

3. **Verify CORS Config**:
   ```java
   // CorsConfig.java
   configuration.setAllowedOrigins(Arrays.asList(
       "https://restaurant-platform.com"
   ));
   ```

4. **Test CORS**:
   ```bash
   curl -H "Origin: https://restaurant-platform.com" \
        -H "Access-Control-Request-Method: GET" \
        -X OPTIONS https://[backend-url]/api/restaurants \
        -v
   ```

5. **For Development**:
   ```bash
   # Create .env file
   CORS_ALLOWED_ORIGINS=http://localhost:3000,http://localhost:4200
   ```

---

### Issue: API Calls Return 404

**Error:**
```
404 Not Found - POST /api/users
```

**Causes:**
- Wrong backend URL in environment.prod.ts
- Backend endpoint not implemented
- Wrong API path

**Solutions:**

1. **Verify Backend URL**:
   ```typescript
   // environment.prod.ts
   export const environment = {
     production: true,
     apiBaseUrl: 'https://[your-render-service].onrender.com/api'
   };
   ```

2. **Check Service Implementation**:
   ```bash
   # Backend should have endpoint
   @PostMapping("/users")
   public ResponseEntity<?> createUser(...) { ... }
   ```

3. **Test Endpoint**:
   ```bash
   curl https://[backend-url]/api/users
   ```

4. **Check Network Tab**:
   - Browser DevTools → Network tab
   - Click request
   - Check:
     - URL is correct
     - Method is correct (GET/POST/PUT)
     - Headers look correct

---

## Database Issues

### Issue: Supabase Connection Timeout

**Error:**
```
Connection to localhost:5432 refused
Database not responding
```

**Causes:**
- Supabase instance paused
- Network connectivity issue
- Wrong credentials

**Solutions:**

1. **Check Supabase Status**:
   - Dashboard → Project Settings
   - Ensure project is "Active" (not paused)

2. **Verify Credentials**:
   - Go to Settings → Database
   - Copy exact host, user, password
   - Ensure no typos

3. **Test Connection**:
   ```bash
   psql -h [host] -U postgres -d postgres -c "SELECT 1"
   ```

4. **Check Network**:
   - If on VPN/proxy, may need IP whitelist
   - Supabase allows all IPs by default

---

### Issue: No Database Data After Deployment

**Problem:**
- Migrations ran
- App deployed successfully
- But database tables are empty

**Causes:**
- Looking at wrong database
- Data truncated during migration
- Seed data not configured

**Solutions:**

1. **Verify Database**:
   - Render settings → Environment variables
   - Confirm using correct Supabase database

2. **Check Migrations**:
   - Supabase SQL editor
   - Run: `SELECT * FROM flyway_schema_history;`
   - Should show all migrations completed

3. **Seed Data**:
   - Create V6__seed_data.sql
   - Add initial data for testing
   - Will run automatically on deploy

---

## Deployment Issues

### Issue: Render Deployment Fails

**Error:**
```
Build failed with exit code 1
Container failed to start
Health check failed
```

**Solutions:**

1. **Check Logs**:
   - Render Dashboard → Logs
   - Scroll to find actual error

2. **Common Causes**:
   - Database credentials invalid
   - Port already in use
   - Missing environment variables
   - Wrong Dockerfile

3. **Verify Configuration**:
   - Environment variables all set
   - Dockerfile is valid
   - Health check path is correct

4. **Try Manual Build**:
   ```bash
   # Locally build Docker image
   docker build -t restaurant-backend ./backend
   docker run -p 8080:8080 restaurant-backend
   ```

---

### Issue: Vercel Deployment Fails

**Error:**
```
Build failed
npm ERR! 404 Not Found
```

**Solutions:**

1. **Check Dependencies**:
   ```bash
   npm install
   npm run build
   ```

2. **Check Build Logs**:
   - Vercel Dashboard → Deployments
   - Expand build logs

3. **Common Issues**:
   - Node version mismatch
   - Missing environment variables
   - TypeScript compilation errors

4. **Clear Cache**:
   - Vercel Dashboard → Settings → Git
   - Redeploy

---

## Performance Issues

### Issue: Slow API Responses

**Causes:**
- Database queries not optimized
- Missing indexes
- Large result sets

**Solutions:**

1. **Add Indexes** (already in V5):
   ```sql
   CREATE INDEX idx_restaurant_city ON restaurant(city);
   CREATE INDEX idx_rating_restaurant ON rating(restaurant_id);
   ```

2. **Optimize Queries**:
   - Use pagination
   - Add WHERE clauses
   - Use database EXPLAIN ANALYZE

3. **Monitor Performance**:
   - Supabase → Insights → Slow Queries
   - Check Render metrics

---

### Issue: High Memory Usage

**Causes:**
- Large datasets in memory
- Memory leaks
- Too many connections

**Solutions:**

1. **Spring Boot Tuning**:
   ```properties
   server.tomcat.max-threads=200
   spring.jpa.properties.hibernate.jdbc.batch_size=20
   ```

2. **Upgrade Plan**:
   - Free tier: 512MB RAM
   - Paid tier: More resources

---

## Getting Help

If you're stuck:

1. **Check Logs First**:
   - Render Logs
   - Vercel Logs
   - Browser Console (DevTools)

2. **Verify Credentials**:
   - All environment variables set
   - No typos in URLs

3. **Test Locally**:
   - Run backend: `java -jar target/*.jar`
   - Run frontend: `npm start`
   - Test with curl/Postman

4. **Contact Support**:
   - [Render Support](https://render.com/support)
   - [Vercel Support](https://vercel.com/support)
   - [Supabase Support](https://supabase.com/support)

---

## Useful Commands

```bash
# Test backend
curl http://localhost:8080/api/actuator/health

# Test database
psql -h [host] -U postgres -d postgres -c "SELECT version();"

# Build frontend
npm run build

# Build backend
mvn clean package -DskipTests

# View Docker logs
docker logs [container-id]

# SSH into Render
# (available on paid plans)
```

---

**Still stuck?** Review the [DEPLOYMENT_GUIDE.md](./DEPLOYMENT_GUIDE.md) or [QUICK_REFERENCE.md](./QUICK_REFERENCE.md)!
