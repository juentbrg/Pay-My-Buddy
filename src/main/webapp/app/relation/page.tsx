"use client"

import Navigation from "../../components/Navigation";
import withAuth from "@/components/withAuth";

const Relation = () => {
    return (
        <>
            <Navigation />
            <main>
                <h1>relation</h1>
            </main>
        </>
    )
}

export default withAuth(Relation);
