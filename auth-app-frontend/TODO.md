## TODO: Complete Auth App

✅ **1. Analyze files & create plan** (Done)

✅ **2. Fix src/Auth/Store.tsx** (Done)
- Used proper AuthStatus enum
- Fixed logout logic
- Fixed checkLogin()
**2. Fix src/Auth/Store.tsx** 
- Use proper AuthStatus enum
- Fix logout logic (handle silent, remove duplicate calls)
- Remove console.logs
- Fix checkLogin(): always boolean
- Add initial auth check

**3. Polish src/Config/ApiClient.ts** 
- Remove unused localStorage token reference

**4. Fix src/components/ui/navbar.tsx** 
- Await logout() in onClick
- Add loading state if needed

**5. Fix src/pages/SignUp.tsx** 
- Fix typos (seError -> setError, navigator -> navigate)
- Auto-login via store after register
- Consistent error handling

**6. Enhance src/pages/Users/UserProfile.tsx** 
- Fix image src
- Make fields editable
- Add update profile API/service call

**7. Improve src/pages/Users/UserHome.tsx** 
- Auto-fetch user on mount
- Polish hardcoded stats or stub API

**8. Minor fixes** 
- src/pages/Login.tsx: remove comments
- src/pages/Users/UserLayout.tsx: handle undefined checkLogin

**9. Cleanup consoles across files**
**10. Test: npm run dev**
**11. Complete!**

