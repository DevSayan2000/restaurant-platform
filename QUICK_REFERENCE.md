# 🚀 Quick Deployment Reference Card

**Restaurant Platform - One-Page Deployment Guide**

---

## 📍 URLs After Deployment

| Service | URL Pattern |
|---------|------------|
| Frontend | `https://restaurant-platform.com` |
| Backend | `https://[render-service].onrender.com/api` |
| Health | `https://[render-service].onrender.com/api/actuator/health` |
| Database | `postgresql://[host]:5432/postgres` |

---

## 1️⃣ Supabase Setup (5 min)

1. Go to [Supabase](https://supabase.com)
2. Click **"New Project"**
3. Fill: Name, Password, Region
4. Save credentials: **Host, Username, Password**

---

## 2️⃣ Backend Deployment - Render (15 min)

```bash
# Step 1: Push to GitHub
git add .
git commit -m "chore: prepare for deployment"
git push origin main

# Step 2: Go to Render
# Dashboard → New Web Service → GitHub Repo

# Step 3: Configure
Name: restaurant-backend
Branch: main
Root Directory: backend
Environment: Docker

# Step 4: Environment Variables
SPRING_DATASOURCE_URL=jdbc:postgresql://[HOST]:5432/postgres
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=[PASSWORD]
CORS_ALLOWED_ORIGINS=http://localhost:3000,http://localhost:4200
SPRING_PROFILES_ACTIVE=prod

# Step 5: Wait for deployment ✅
# Check: [your-backend-url].onrender.com/api/actuator/health
```

---

## 3️⃣ Frontend Deployment - Vercel (10 min)

```typescript
// Step 1: Update environment.prod.ts
export const environment = {
  production: true,
  apiBaseUrl: 'https://[your-render-service].onrender.com/api',
};

// Step 2: Push to GitHub
git add .
git commit -m "chore: update backend URL"
git push origin main

// Step 3: Go to Vercel → Import GitHub Repo
// Set Root Directory: frontend
// Set Build: npm run build
// Set Output: dist/frontend

// Step 4: Deploy & Wait ✅
```

---

## 4️⃣ Testing Checklist

- [ ] Frontend loads: `https://restaurant-platform.com`
- [ ] Backend health: `curl [render-url]/api/actuator/health`
- [ ] API call works: Open DevTools → Network → Make API call
- [ ] Database works: Check data in Supabase dashboard
- [ ] No CORS errors: Check browser console

---

## 🔄 Update CORS in Production

After frontend URL is known:

```bash
# In Render Dashboard → Environment Variables

CORS_ALLOWED_ORIGINS=https://restaurant-platform.com

# Service auto-restarts
```

---

## 🐛 Quick Troubleshooting

| Issue | Check |
|-------|-------|
| CORS Error | `CORS_ALLOWED_ORIGINS` env var in Render |
| DB Connection Failed | Supabase credentials in env vars |
| Frontend 404 | Vercel output directory: `dist/frontend` |
| API 404 | Backend URL in `environment.prod.ts` |
| Migration Failed | File naming: `V#__description.sql` |

---

## 📊 Key Environment Variables

**Local Development** (in `.env`):
```
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/restaurant_db
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=password
CORS_ALLOWED_ORIGINS=http://localhost:3000,http://localhost:4200
```

**Production** (in Render Dashboard):
```
SPRING_DATASOURCE_URL=jdbc:postgresql://[SUPABASE-HOST]:5432/postgres
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=[YOUR-PASSWORD]
CORS_ALLOWED_ORIGINS=https://restaurant-platform.com
SPRING_PROFILES_ACTIVE=prod
```

---

## 🔗 Documentation Files

| File | Purpose |
|------|---------|
| [DEPLOYMENT_GUIDE.md](./DEPLOYMENT_GUIDE.md) | Complete step-by-step guide |
| [TROUBLESHOOTING.md](./TROUBLESHOOTING.md) | Issue resolution |
| [.env.example](./.env.example) | Environment variables template |

---

## ⚡ Quick Commands

```bash
# Local Development
./start-dev.bat          # Windows
./start-dev.sh          # Linux/Mac

# Manual Start
mvn clean package -DskipTests
java -jar target/*.jar
npm run build
npm start

# Test Backend
curl http://localhost:8080/api/actuator/health

# Test Database
psql -h [host] -U postgres -d postgres -c "SELECT 1"
```

---

## 📋 Deployment Steps Summary

```
1. Supabase: Create project, get credentials
   ↓
2. Render: 
   - Connect GitHub repo
   - Set environment variables
   - Deploy
   ↓
3. Update Frontend: Set backend URL
   ↓
4. Vercel:
   - Import GitHub repo
   - Deploy
   ↓
5. Test: Verify frontend, API, database
   ↓
6. Update CORS: Restrict to frontend domain
```

---

## ⏱️ Total Deployment Time

| Step | Time |
|------|------|
| Supabase Setup | 5 min |
| Backend Render | 15 min |
| Frontend Vercel | 10 min |
| Testing | 5 min |
| **Total** | **~35 min** |

---

## 🎯 Success Indicators

✅ Frontend loads without errors
✅ Backend health check returns 200
✅ API calls succeed (Network tab shows 200)
✅ Database queries work
✅ No CORS errors in console
✅ Data persists in database

---

## 🆘 Need Help?

1. Check **TROUBLESHOOTING.md**
2. Review **DEPLOYMENT_GUIDE.md**
3. Check platform docs:
   - [Render Docs](https://render.com/docs)
   - [Vercel Docs](https://vercel.com/docs)
   - [Supabase Docs](https://supabase.com/docs)

---

**Print this card for quick reference during deployment! 📲**

---

**Ready to Deploy?** Start with the [DEPLOYMENT_GUIDE.md](./DEPLOYMENT_GUIDE.md)

Last Updated: March 1, 2026
