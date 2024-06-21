"use client"

import { useRouter } from "next/navigation";
import React, { useEffect, useState } from "react";
import axios from "axios";

const withAuth = <P extends object>(WrappedComponent: React.ComponentType<P>): React.FC<P> => {
    const ComponentWithAuth = (props: P) => {
        const [loading, setLoading] = useState(true);
        const router = useRouter();

        useEffect(() => {
            const checkAuth = async () => {
                try {
                    const response = await axios.get('http://localhost:8080/api/auth/session', { withCredentials: true });
                    if (response.status === 200) {
                        setLoading(false);
                    } else {
                        router.push('/login');
                    }
                } catch (error) {
                    router.push('/login');
                }
            };

            checkAuth();
        }, [router]);

        if (loading) {
            return null;
        }
        return <WrappedComponent {...props} />;
    };

    return ComponentWithAuth;
};

export default withAuth;
