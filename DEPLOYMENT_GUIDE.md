# 🚀 Full Deployment Guide: Angular + Spring Boot + Postgres

This comprehensive guide walks you through containerizing and deploying your Restaurant Platform using:
- **Backend** → Render (Spring Boot)
- **Frontend** → Vercel (Angular)
- **Database** → Supabase (PostgreSQL)
- **Migrations** → Flyway

---

## 📋 Table of Contents
1. [Prerequisites](#prerequisites)
2. [Project Preparation](#project-preparation)
3. [Database Setup (Supabase)](#database-setup-supabase)
4. [Backend Deployment (Render)](#backend-deployment-render)
5. [Frontend Deployment (Vercel)](#frontend-deployment-vercel)
6. [Testing & Verification](#testing--verification)
7. [Common Issues & Solutions](#common-issues--solutions)

---

## 🔧 Prerequisites

Ensure you have:
- **Git** installed and repository pushed to GitHub
- **Node.js** (v20+) for frontend
- **Java 21** and Maven for backend
- **Docker** (optional, for local testing)
- Accounts on:
  - [GitHub](https://github.com)
  - [Supabase](https://supabase.com)
  - [Render](https://render.com)
  - [Vercel](https://vercel.com)

---

## 🧱 Project Preparation

### ✅ What's Already Done:

1. **pom.xml** - Added dependencies:
   - Spring Boot Actuator (health checks)
   - Flyway (database migrations)

2. **application.properties** - Configured:
   - Environment variables for database
   - Flyway migrations
   - Actuator endpoints
   - CORS settings

3. **Backend Dockerfile** - Multi-stage build for optimal image size

4. **CORS Configuration** - CorsConfig.java for cross-origin requests

5. **Database Migrations** - Flyway migration files:
   - V1: Users table
   - V2: Restaurant table
   - V3: Rating table
   - V5: Indexes

6. **Environment Files** - Updated for production

---

## 🗄️ Step 1: Database Setup (Supabase)

### 1.1 Create Supabase Project

1. Go to [Supabase](https://supabase.com)
2. Click **"New Project"**
3. Fill in:
   - **Name**: `restaurant-platform`
   - **Database Password**: Save securely
   - **Region**: Choose closest to your users
4. Click **"Create new project"** and wait for initialization

### 1.2 Get Database Credentials

Once project is created, go to **Settings → Database**:

```
Host: <YOUR-PROJECT-ID>.supabase.co
Port: 5432
Username: postgres
Password: <YOUR-PASSWORD>
Database: postgres
```

Copy these values - you'll need them for deployment.

### 1.3 Verify Migrations Will Run Automatically

The Flyway migrations will run automatically when the backend starts. No manual intervention needed!

---

## 🚀 Step 2: Backend Deployment (Render)

### 2.1 Push Code to GitHub

```bash
git add .
git commit -m "chore: prepare for deployment"
git push origin main
```

### 2.2 Deploy on Render

1. Go to [Render Dashboard](https://dashboard.render.com)
2. Click **"New +"** → **"Web Service"**
3. Select your GitHub repository
4. Configure:
   - **Name**: `restaurant-backend`
   - **Branch**: `main`
   - **Root Directory**: `backend`
   - **Environment**: `Docker`
   - **Plan**: Free (or Paid for production)

5. Click **"Create Web Service"**

### 2.3 Set Environment Variables

In the Render dashboard, go to **Settings** and add under **Environment**:

```
SPRING_DATASOURCE_URL=jdbc:postgresql://<SUPABASE-HOST>:5432/postgres
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=<YOUR-PASSWORD>
CORS_ALLOWED_ORIGINS=http://localhost:3000,http://localhost:4200,https://restaurant-platform.com
SPRING_PROFILES_ACTIVE=prod
```

### 2.4 Wait for Deployment

- Render will build and deploy automatically
- Check the **Logs** tab for any errors
- Once healthy, you'll see a green "Running" status
- Your backend URL will be: `https://api.restaurant-platform.com/api`

### 2.5 Verify Health Check

Visit: `https://api.restaurant-platform.com/api/actuator/health`

You should see:
```json
{
  "status": "UP",
  "components": {
    "db": {"status": "UP"},
    "flyway": {"status": "UP"},
    "ping": {"status": "UP"}
  }
}
```

---

## 🌍 Step 3: Frontend Deployment (Vercel)

### 3.1 Update Backend URL

Update `frontend/src/environments/environment.prod.ts`:

```typescript
export const environment = {
  production: true,
  apiBaseUrl: 'https://api.restaurant-platform.com/api',
};
```

### 3.2 Commit and Push

```bash
git add frontend/src/environments/environment.prod.ts
git commit -m "chore: update backend URL for production"
git push origin main
```

### 3.3 Deploy on Vercel

1. Go to [Vercel Dashboard](https://vercel.com/dashboard)
2. Click **"Add New"** → **"Project"**
3. **Import** your GitHub repository
4. Configure:
   - **Framework**: Angular
   - **Root Directory**: `frontend`
   - **Build Command**: `npm run build`
   - **Output Directory**: `dist/frontend`

5. Add **Environment Variables** (if needed):
   ```
   NG_BUILD_CONFIGURATION=production
   ```

6. Click **"Deploy"**

### 3.4 Wait for Deployment

- Vercel will build and deploy
- Visit your frontend URL: `https://restaurant-platform.com`

---

## 🧪 Step 4: Testing & Verification

### 4.1 Frontend Loads

- Visit your frontend URL
- Page should load without 404 errors
- Check browser console (F12) for errors

### 4.2 API Communication

1. Open browser DevTools (F12)
2. Go to **Network** tab
3. Perform an action that calls the API
4. Verify:
   - Status: `200` (not 404, 500, or CORS errors)
   - Response contains expected data

### 4.3 Database Writes/Reads

- Test CRUD operations in your app
- Verify data persists
- Check Supabase dashboard for data

### 4.4 Health Check

```bash
curl https://api.restaurant-platform.com/api/actuator/health
```

Should return:
```json
{"status": "UP"}
```

---

## 🔐 Step 5: CORS Configuration

### Current Setup

The `CorsConfig.java` handles CORS at application level with:
- Allowed origins from `CORS_ALLOWED_ORIGINS` environment variable
- All standard HTTP methods
- Credentials support

### Production Recommendations

For **maximum security**, update CORS to only allow your frontend:

```properties
# In Render environment variables
CORS_ALLOWED_ORIGINS=https://restaurant-platform.com
```

Or update `application.properties`:

```properties
app.cors.allowed-origins=https://restaurant-platform.com
```

### Restrict to Specific Origins

```java
// In CorsConfig.java for stricter control
configuration.setAllowedOrigins(Arrays.asList(
    "https://restaurant-platform.com"
));
```

---

## ⚠️ Common Issues & Solutions

### Issue 1: CORS Errors

**Error**: `Access to XMLHttpRequest blocked by CORS policy`

**Solution**:
1. Check `CORS_ALLOWED_ORIGINS` environment variable includes your frontend URL
2. Ensure frontend URL is correct in browser
3. Restart Render service after changing environment variables

### Issue 2: Database Connection Failed

**Error**: `org.postgresql.util.PSQLException: Connection refused`

**Solution**:
1. Verify Supabase project is active
2. Check credentials are correct
3. Verify `SPRING_DATASOURCE_URL` format: `jdbc:postgresql://HOST:5432/postgres`
4. Check firewall: Supabase allows connections from Render

### Issue 3: Flyway Migration Fails

**Error**: `Flyway Migration failed` or `schema version doesn't match`

**Solution**:
1. Check migration file naming: Must be `V#__description.sql`
2. Verify SQL syntax is valid PostgreSQL
3. Check for duplicate version numbers
4. For existing databases: Use `spring.flyway.baseline-on-migrate=true`

### Issue 4: Frontend Shows 404

**Error**: Blank page or "404 Not Found"

**Solution**:
1. Verify Vercel build output directory is `dist/frontend`
2. Check build logs for compilation errors
3. Ensure `angular.json` is correctly configured

### Issue 5: API Calls Return 404

**Error**: `POST /api/users 404`

**Solution**:
1. Verify backend URL in `environment.prod.ts` is correct
2. Check backend has the endpoint implemented
3. Verify API path includes `/api` prefix

### Issue 6: Deployment Takes Too Long / Times Out

**Solution for Render**:
1. Upgrade from Free to Paid plan
2. Database query optimization
3. Reduce JAR size by excluding unnecessary dependencies

---

## 📊 Architecture Diagram

```
┌─────────────────────────┐
│   User Browser          │
│                         │
└────────────┬────────────┘
             │ HTTPS
             ▼
┌─────────────────────────────────┐
│   Frontend (Vercel)             │
│   - Angular Application         │
│   - dist/frontend               │
└────────────┬────────────────────┘
             │ API Calls (HTTPS)
             ▼
┌─────────────────────────────────┐
│   Backend (Render)              │
│   - Spring Boot Application     │
│   - Port 8080 → /api            │
│   - Actuator Health Checks      │
└────────────┬────────────────────┘
             │ JDBC (SSL)
             ▼
┌─────────────────────────────────┐
│   Database (Supabase)           │
│   - PostgreSQL                  │
│   - Flyway Migrations           │
└─────────────────────────────────┘
```

---

## 🎯 Post-Deployment Checklist

- [ ] Backend health check returns 200
- [ ] Frontend loads without errors
- [ ] API calls succeed (check Network tab)
- [ ] Database queries work
- [ ] CORS errors resolved
- [ ] Environment variables are set
- [ ] Database migrations ran successfully
- [ ] Custom domain configured (optional)
- [ ] Monitoring/logging enabled
- [ ] Backup strategy in place

---

## 📈 Optional Improvements

### 1. Custom Domain

**Render**:
1. Go to **Settings** → **Custom Domain**
2. Add your domain
3. Update DNS records as instructed

**Vercel**:
1. Go to **Settings** → **Domains**
2. Add your domain
3. Update DNS records

### 2. CI/CD Pipeline (GitHub Actions)

Create `.github/workflows/deploy.yml`:

```yaml
name: Deploy
on:
  push:
    branches: [main]
jobs:
  deploy:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - name: Deploy to Render
        run: curl -X POST ${{ secrets.RENDER_DEPLOY_HOOK }}
```

### 3. HTTPS Enforcement

Add to `application.properties`:

```properties
server.servlet.session.cookie.secure=true
server.servlet.session.cookie.http-only=true
```

### 4. Monitoring

- **Render**: Built-in logs and metrics
- **Vercel**: Analytics and logs
- **Supabase**: Database monitoring dashboard

### 5. Database Backups

Supabase provides automatic daily backups. Access in **Settings → Backups**.

---

## 🔗 Useful Resources

- [Render Documentation](https://render.com/docs)
- [Vercel Documentation](https://vercel.com/docs)
- [Supabase Documentation](https://supabase.com/docs)
- [Flyway Documentation](https://flywaydb.org/documentation/)
- [Spring Boot Docker Guide](https://spring.io/guides/topical/spring-boot-docker/)
- [Angular Deployment Guide](https://angular.io/guide/deployment)

---

## 💡 Tips for Success

1. **Start with Dev Databases**: Test in lower environments first
2. **Monitor Logs**: Check Render/Vercel logs when issues arise
3. **Version Control**: Keep all configs in Git (except secrets)
4. **Backup Database**: Regular backups before major changes
5. **Test End-to-End**: Always test full flow after deployment
6. **Use Environment Variables**: Never hardcode secrets
7. **Keep Dependencies Updated**: Regular security updates

---

**Happy Deploying! 🎉**

For issues or questions, refer to the respective platform documentation or GitHub issues.
