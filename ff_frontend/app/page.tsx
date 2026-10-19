import React from "react";
//import { motion } from "framer-motion";
import {
  ArrowRight,
  BarChart3,
  Bolt,
  ChevronRight,
  CircleDollarSign,
  Crown,
  Flame,
  LayoutGrid,
  LayoutDashboard,
  LucideIcon,
  Medal,
  Radar,
  Search,
  Settings,
  ShieldCheck,
  Star,
  Trophy,
  Users,
  Zap,
} from "lucide-react";

const navItems: { label: string; icon: LucideIcon; active?: boolean }[] = [
  { label: "Dashboard", icon: LayoutDashboard, active: true },
  { label: "Drivers", icon: LayoutGrid },
  { label: "My Team", icon: ShieldCheck },
  { label: "Race Center", icon: Radar },
  { label: "Leaderboards", icon: Trophy },
  { label: "AI Strategy Hub", icon: Zap },
  { label: "Settings", icon: Settings },
];

const drivers = [
  { name: "Max Verstappen", team: "Red Bull", price: "$30M", form: 98, points: 342, badge: "Captain" },
  { name: "Lando Norris", team: "McLaren", price: "$27M", form: 93, points: 315, badge: "Hot" },
  { name: "George Russell", team: "Mercedes", price: "$20M", form: 82, points: 254, badge: "Value" },
  { name: "Oscar Piastri", team: "McLaren", price: "$24M", form: 90, points: 301, badge: "Top Pick" },
];

const leaderboard = [
  { rank: 1, user: "LewisFan99", points: 984 },
  { rank: 2, user: "Abhay", points: 950 },
  { rank: 3, user: "MaxAttack", points: 931 },
  { rank: 4, user: "GridGenius", points: 908 },
];

function StatCard({
  title,
  value,
  sub,
  icon: Icon,
}: {
  title: string;
  value: string;
  sub: string;
  icon: LucideIcon;
}) {
  return (
    <div className="rounded-3xl border border-white/10 bg-white/5 p-5 shadow-[0_20px_80px_rgba(0,0,0,0.25)] backdrop-blur-xl">
      <div className="flex items-start justify-between gap-4">
        <div>
          <p className="text-sm text-white/60">{title}</p>
          <div className="mt-2 text-3xl font-semibold tracking-tight text-white">{value}</div>
          <p className="mt-2 text-sm text-white/55">{sub}</p>
        </div>
        <div className="rounded-2xl border border-white/10 bg-black/20 p-3 text-white/80">
          <Icon className="h-5 w-5" />
        </div>
      </div>
    </div>
  );
}

function DriverCard({
  name,
  team,
  price,
  form,
  points,
  badge,
}: {
  name: string;
  team: string;
  price: string;
  form: number;
  points: number;
  badge: string;
}) {
  return (
    <div className="rounded-3xl border border-white/10 bg-white/5 p-5 transition hover:-translate-y-0.5 hover:bg-white/8">
      <div className="flex items-center justify-between gap-3">
        <div>
          <div className="flex items-center gap-2">
            <h3 className="text-lg font-semibold text-white">{name}</h3>
            <span className="rounded-full border border-red-500/30 bg-red-500/10 px-2.5 py-0.5 text-[11px] font-medium text-red-200">
              {badge}
            </span>
          </div>
          <p className="mt-1 text-sm text-white/60">{team}</p>
        </div>
        <div className="rounded-2xl bg-black/20 px-3 py-2 text-right">
          <p className="text-xs text-white/50">Price</p>
          <p className="text-sm font-semibold text-white">{price}</p>
        </div>
      </div>

      <div className="mt-5 grid grid-cols-3 gap-3">
        <div className="rounded-2xl bg-black/20 p-3">
          <p className="text-[11px] uppercase tracking-wider text-white/45">Form</p>
          <p className="mt-1 text-xl font-semibold text-white">{form}</p>
        </div>
        <div className="rounded-2xl bg-black/20 p-3">
          <p className="text-[11px] uppercase tracking-wider text-white/45">Points</p>
          <p className="mt-1 text-xl font-semibold text-white">{points}</p>
        </div>
        <div className="rounded-2xl bg-black/20 p-3">
          <p className="text-[11px] uppercase tracking-wider text-white/45">Status</p>
          <p className="mt-1 text-xl font-semibold text-emerald-300">Live</p>
        </div>
      </div>
    </div>
  );
}

function App() {
  return (
    <div className="min-h-screen bg-[#07111f] text-white">
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute -top-24 left-1/2 h-72 w-72 -translate-x-1/2 rounded-full bg-red-500/15 blur-3xl" />
        <div className="absolute right-0 top-32 h-80 w-80 rounded-full bg-blue-500/10 blur-3xl" />
        <div className="absolute bottom-0 left-0 h-80 w-80 rounded-full bg-emerald-400/10 blur-3xl" />
      </div>

      <div className="relative mx-auto flex min-h-screen max-w-[1600px] gap-6 p-6 lg:p-8">
        {/* Sidebar */}
        <aside className="hidden w-[280px] shrink-0 flex-col rounded-[32px] border border-white/10 bg-white/5 p-5 shadow-[0_20px_80px_rgba(0,0,0,0.25)] backdrop-blur-xl lg:flex">
          <div className="flex items-center gap-3 px-1 py-1">
            <div className="flex h-12 w-12 items-center justify-center rounded-2xl bg-red-500 text-black shadow-lg shadow-red-500/30">
              <Flame className="h-6 w-6" />
            </div>
            <div>
              <p className="text-lg font-semibold leading-none">ApexGrid</p>
              <p className="mt-1 text-sm text-white/55">Fantasy Formula Racing</p>
            </div>
          </div>

          <div className="mt-6 rounded-3xl border border-white/10 bg-black/20 p-4">
            <div className="flex items-center justify-between text-sm text-white/60">
              <span>Season Progress</span>
              <span>62%</span>
            </div>
            <div className="mt-3 h-2 overflow-hidden rounded-full bg-white/10">
              <div className="h-full w-[62%] rounded-full bg-gradient-to-r from-red-500 to-orange-400" />
            </div>
            <div className="mt-4 flex items-center justify-between text-sm">
              <span className="text-white/55">Points</span>
              <span className="font-semibold text-white">950</span>
            </div>
          </div>

          <nav className="mt-6 space-y-2">
            {navItems.map((item) => {
              const Icon = item.icon;
              return (
                <button
                  key={item.label}
                  className={`flex w-full items-center gap-3 rounded-2xl px-4 py-3 text-left transition ${
                    item.active
                      ? "bg-red-500 text-black shadow-lg shadow-red-500/20"
                      : "text-white/70 hover:bg-white/7"
                  }`}
                >
                  <Icon className="h-4 w-4" />
                  <span className="text-sm font-medium">{item.label}</span>
                </button>
              );
            })}
          </nav>

          <div className="mt-auto rounded-3xl border border-white/10 bg-gradient-to-br from-white/10 to-white/5 p-5">
            <div className="flex items-center gap-2 text-sm text-white/75">
              <SparkIcon />
              AI Assistant
            </div>
            <p className="mt-3 text-sm leading-6 text-white/55">
              Ask for historic stats, form analysis, and captain suggestions.
            </p>
            <button className="mt-4 inline-flex items-center gap-2 rounded-2xl bg-white px-4 py-2.5 text-sm font-semibold text-black">
              Coming Soon <ArrowRight className="h-4 w-4" />
            </button>
          </div>
        </aside>

        {/* Main content */}
        <main className="flex-1 space-y-6">
          {/* Top bar */}
          <header className="flex flex-col gap-4 rounded-[32px] border border-white/10 bg-white/5 p-5 shadow-[0_20px_80px_rgba(0,0,0,0.25)] backdrop-blur-xl md:flex-row md:items-center md:justify-between">
            <div>
              <p className="text-sm text-white/55">Welcome back</p>
              <h1 className="mt-1 text-3xl font-semibold tracking-tight md:text-4xl">
                Fantasy Formula Racing Dashboard
              </h1>
            </div>

            <div className="flex flex-wrap items-center gap-3">
              <div className="flex items-center gap-2 rounded-2xl border border-white/10 bg-black/20 px-4 py-3 text-sm text-white/60">
                <Search className="h-4 w-4" />
                Search drivers, teams, leagues...
              </div>
              <button className="rounded-2xl bg-red-500 px-4 py-3 text-sm font-semibold text-black shadow-lg shadow-red-500/20">
                Create Team
              </button>
            </div>
          </header>

          {/* Hero */}
          <section className="grid gap-6 xl:grid-cols-[1.55fr_0.85fr]">
            <div className="rounded-[32px] border border-white/10 bg-gradient-to-br from-white/10 to-white/5 p-6 shadow-[0_20px_80px_rgba(0,0,0,0.25)] backdrop-blur-xl">
              <div className="flex flex-col justify-between gap-6 lg:flex-row lg:items-end">
                <div className="max-w-2xl">
                  <div className="inline-flex items-center gap-2 rounded-full border border-red-500/30 bg-red-500/10 px-3 py-1 text-xs font-medium text-red-200">
                    <Bolt className="h-3.5 w-3.5" />
                    Live race scoring
                  </div>
                  <h2 className="mt-4 text-4xl font-semibold tracking-tight text-white md:text-5xl">
                    Build your dream grid.
                    <br />
                    Compete every race weekend.
                  </h2>
                  <p className="mt-4 max-w-xl text-base leading-7 text-white/60">
                    A premium fantasy motorsport experience with live telemetry, driver analytics,
                    private leagues, and AI-assisted strategy in phase two.
                  </p>
                  <div className="mt-6 flex flex-wrap gap-3">
                    <button className="rounded-2xl bg-white px-5 py-3 text-sm font-semibold text-black">
                      View My Team
                    </button>
                    <button className="rounded-2xl border border-white/10 bg-black/20 px-5 py-3 text-sm font-semibold text-white/85">
                      Explore Drivers
                    </button>
                  </div>
                </div>

                <div className="grid grid-cols-2 gap-3 sm:min-w-[320px]">
                  <div className="rounded-3xl bg-black/20 p-4">
                    <p className="text-xs uppercase tracking-wider text-white/45">Next Race</p>
                    <p className="mt-2 text-xl font-semibold">Belgian GP</p>
                    <p className="mt-2 text-sm text-white/55">Qualifying in 2 days</p>
                  </div>
                  <div className="rounded-3xl bg-black/20 p-4">
                    <p className="text-xs uppercase tracking-wider text-white/45">Current Rank</p>
                    <p className="mt-2 text-xl font-semibold">134</p>
                    <p className="mt-2 text-sm text-emerald-300">+18 this week</p>
                  </div>
                  <div className="rounded-3xl bg-black/20 p-4">
                    <p className="text-xs uppercase tracking-wider text-white/45">Budget Left</p>
                    <p className="mt-2 text-xl font-semibold">$6.5M</p>
                    <p className="mt-2 text-sm text-white/55">2 transfers remaining</p>
                  </div>
                  <div className="rounded-3xl bg-black/20 p-4">
                    <p className="text-xs uppercase tracking-wider text-white/45">Captain</p>
                    <p className="mt-2 text-xl font-semibold">Norris</p>
                    <p className="mt-2 text-sm text-white/55">2x points active</p>
                  </div>
                </div>
              </div>
            </div>

            {/* Right column */}
            <div className="space-y-6">
              <div className="rounded-[32px] border border-white/10 bg-white/5 p-6 shadow-[0_20px_80px_rgba(0,0,0,0.25)] backdrop-blur-xl">
                <div className="flex items-center justify-between">
                  <div>
                    <p className="text-sm text-white/55">Season Points</p>
                    <p className="mt-1 text-3xl font-semibold">950</p>
                  </div>
                  <div className="rounded-2xl bg-emerald-400/15 p-3 text-emerald-300">
                    <Medal className="h-5 w-5" />
                  </div>
                </div>
                <div className="mt-5 h-40 rounded-3xl border border-white/10 bg-black/20 p-4">
                  <div className="flex h-full items-end gap-2">
                    {[38, 42, 52, 44, 58, 64, 72].map((h, i) => (
                      <div key={i} className="flex-1 rounded-t-2xl bg-gradient-to-t from-red-500 to-orange-300" style={{ height: `${h}%` }} />
                    ))}
                  </div>
                </div>
              </div>

              <div className="rounded-[32px] border border-white/10 bg-white/5 p-6 shadow-[0_20px_80px_rgba(0,0,0,0.25)] backdrop-blur-xl">
                <div className="flex items-center justify-between">
                  <div>
                    <p className="text-sm text-white/55">AI Strategy Hub</p>
                    <p className="mt-1 text-2xl font-semibold">Coming Next</p>
                  </div>
                  <div className="rounded-2xl bg-white/10 p-3 text-white/80">
                    <Radar className="h-5 w-5" />
                  </div>
                </div>
                <p className="mt-4 text-sm leading-6 text-white/60">
                  Historic F1 analysis, transfer advice, captain recommendations, and personalized
                  race-weekend predictions.
                </p>
                <button className="mt-5 inline-flex items-center gap-2 rounded-2xl bg-white px-4 py-3 text-sm font-semibold text-black">
                  Preview concept <ChevronRight className="h-4 w-4" />
                </button>
              </div>
            </div>
          </section>

          {/* Middle section */}
          <section className="grid gap-6 xl:grid-cols-[1.25fr_0.75fr]">
            <div className="space-y-6">
              <div className="grid gap-4 md:grid-cols-2 xl:grid-cols-4">
                <StatCard title="Current Rank" value="134" sub="Global leaderboard" icon={Crown} />
                <StatCard title="Fantasy Points" value="950" sub="Season total" icon={BarChart3} />
                <StatCard title="Budget Left" value="$6.5M" sub="Transfers available" icon={CircleDollarSign} />
                <StatCard title="Private League" value="12" sub="Friends competing" icon={Users} />
              </div>

              <div className="rounded-[32px] border border-white/10 bg-white/5 p-6 shadow-[0_20px_80px_rgba(0,0,0,0.25)] backdrop-blur-xl">
                <div className="flex items-center justify-between gap-4">
                  <div>
                    <p className="text-sm text-white/55">My Team</p>
                    <h3 className="mt-1 text-2xl font-semibold">ApexGrid Racing XI</h3>
                  </div>
                  <button className="rounded-2xl border border-white/10 bg-black/20 px-4 py-2.5 text-sm font-medium text-white/80">
                    Edit Team
                  </button>
                </div>

                <div className="mt-5 grid gap-4 md:grid-cols-2">
                  {drivers.map((d) => (
                    <DriverCard key={d.name} {...d} />
                  ))}
                </div>
              </div>
            </div>

            <div className="space-y-6">
              <div className="rounded-[32px] border border-white/10 bg-white/5 p-6 shadow-[0_20px_80px_rgba(0,0,0,0.25)] backdrop-blur-xl">
                <div className="flex items-center justify-between">
                  <div>
                    <p className="text-sm text-white/55">Leaderboard</p>
                    <h3 className="mt-1 text-2xl font-semibold">Top Players</h3>
                  </div>
                  <button className="text-sm text-white/60">
                    View all
                  </button>
                </div>

                <div className="mt-5 space-y-3">
                  {leaderboard.map((row) => (
                    <div key={row.rank} className="flex items-center justify-between rounded-2xl border border-white/10 bg-black/20 px-4 py-3">
                      <div className="flex items-center gap-3">
                        <div className="flex h-9 w-9 items-center justify-center rounded-xl bg-white/10 text-sm font-semibold text-white/85">
                          {row.rank}
                        </div>
                        <div>
                          <p className="font-medium text-white">{row.user}</p>
                          <p className="text-xs text-white/50">Season standing</p>
                        </div>
                      </div>
                      <p className="text-sm font-semibold text-white">{row.points} pts</p>
                    </div>
                  ))}
                </div>
              </div>

              <div className="rounded-[32px] border border-white/10 bg-white/5 p-6 shadow-[0_20px_80px_rgba(0,0,0,0.25)] backdrop-blur-xl">
                <div className="flex items-center justify-between">
                  <div>
                    <p className="text-sm text-white/55">Race Weekend</p>
                    <h3 className="mt-1 text-2xl font-semibold">Belgian GP</h3>
                  </div>
                  <div className="rounded-2xl bg-red-500/15 p-3 text-red-200">
                    <Flame className="h-5 w-5" />
                  </div>
                </div>
                <div className="mt-5 space-y-3">
                  {[
                    ["Practice 1", "Tomorrow 12:30"],
                    ["Qualifying", "Saturday 18:00"],
                    ["Race", "Sunday 17:00"],
                  ].map(([label, time]) => (
                    <div key={label} className="flex items-center justify-between rounded-2xl border border-white/10 bg-black/20 px-4 py-3">
                      <div>
                        <p className="font-medium text-white">{label}</p>
                        <p className="text-xs text-white/50">Session schedule</p>
                      </div>
                      <p className="text-sm text-white/70">{time}</p>
                    </div>
                  ))}
                </div>
              </div>
            </div>
          </section>
        </main>
      </div>
    </div>
  );
}

function SparkIcon() {
  return <Star className="h-4 w-4 text-amber-300" />;
}

export default App;
