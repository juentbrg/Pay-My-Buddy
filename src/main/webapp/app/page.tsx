"use client"

import withAuth from "../components/withAuth";
import React from "react";

const Home = () => {
  return (
    <main>
      <h1>Home</h1>
    </main>
  );
}

export default withAuth(Home);
