import React from 'react'
import { Button } from "../ui/button";
import { Card, CardContent } from "../ui/card";
import { motion } from "framer-motion";
import { ShieldCheck, Lock, Zap, Sun, Moon } from "lucide-react";

const FuturisticAuthHome = () => {
  const [theme, setTheme] = React.useState("light");

  React.useEffect(() => {
    const root = window.document.documentElement;
    if (theme === "dark") {
      root.classList.add("dark");
    } else {
      root.classList.remove("dark");
    }
  }, [theme]);

  return (
    <div className="min-h-screen transition-colors duration-500 bg-white text-gray-900 dark:bg-black dark:text-white">
      {/* Navbar */}
      <nav className="flex justify-between items-center px-8 py-6 border-b border-gray-200 dark:border-gray-800">
        <h1 className="text-2xl font-bold tracking-wide">AuthX</h1>
        <div className="flex items-center gap-4">
          <Button
            variant="outline"
            size="icon"
            onClick={() => setTheme(theme === "dark" ? "light" : "dark")}
          >
            {theme === "dark" ? <Sun size={18} /> : <Moon size={18} />}
          </Button>
          <Button variant="ghost">Login</Button>
          <Button>Sign Up</Button>
        </div>
      </nav>

      {/* Hero Section */}
      <section className="text-center py-24 px-6 bg-gradient-to-b from-gray-50 to-white dark:from-gray-900 dark:to-black">
        <motion.h2
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.6 }}
          className="text-5xl font-extrabold mb-6"
        >
          Secure Authentication, Reinvented
        </motion.h2>
        <p className="text-gray-600 dark:text-gray-300 max-w-2xl mx-auto mb-8">
          Experience lightning-fast, secure, and scalable authentication for modern applications.
        </p>
        <div className="flex justify-center gap-4">
          <Button size="lg">Get Started</Button>
          <Button size="lg" variant="outline">Learn More</Button>
        </div>
      </section>

      {/* Features Section */}
      <section className="grid md:grid-cols-3 gap-6 px-8 py-20 bg-white dark:bg-black">
        {[{
          icon: <ShieldCheck size={32} />,
          title: "Advanced Security",
          desc: "Multi-layered protection with encryption and biometrics."
        }, {
          icon: <Lock size={32} />,
          title: "Privacy First",
          desc: "Your data stays yours. We never compromise privacy."
        }, {
          icon: <Zap size={32} />,
          title: "Blazing Fast",
          desc: "Optimized for speed with minimal latency worldwide."
        }].map((feature, i) => (
          <motion.div key={i} whileHover={{ scale: 1.04 }}>
            <Card className="rounded-2xl border border-gray-200 dark:border-gray-800 bg-white dark:bg-gray-900 shadow-sm dark:shadow-lg">
              <CardContent className="p-6 text-center space-y-4">
                <div className="flex justify-center text-indigo-600 dark:text-indigo-400">
                  {feature.icon}
                </div>
                <h3 className="text-xl font-semibold">{feature.title}</h3>
                <p className="text-gray-600 dark:text-gray-400">{feature.desc}</p>
              </CardContent>
            </Card>
          </motion.div>
        ))}
      </section>

      {/* CTA Section */}
      <section className="text-center py-20 px-6 bg-gray-50 dark:bg-gray-900">
        <h3 className="text-3xl font-bold mb-4">Ready to Secure Your App?</h3>
        <p className="text-gray-600 dark:text-gray-400 mb-6">Start integrating in minutes with our powerful API.</p>
        <Button size="lg">Create Free Account</Button>
      </section>

      {/* Footer */}
      <footer className="border-t border-gray-200 dark:border-gray-800 py-6 text-center text-gray-500">
        © {new Date().getFullYear()} AuthX. All rights reserved.
      </footer>
    </div>
  )
}

export default FuturisticAuthHome
