"use client"

import Navigation from "../../components/Navigation";
import withAuth from "@/components/withAuth";

const Profile = () => {
    return (
        <>
            <Navigation />
            <main>
                <h1>profile</h1>
            </main>
        </>
    )
}

export default withAuth(Profile);
