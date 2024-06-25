"use client"

import withAuth from "../components/withAuth";
import React from "react";
import Navigation from "../components/Navigation";
import Transfert from "../components/Transfert"

const Home = () => {
  return (
    <>
        <Navigation />
        <main>
          <Transfert />
        </main>
    </>
  );
}

export default withAuth(Home);
