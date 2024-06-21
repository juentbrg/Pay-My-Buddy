"use client"

import { useRouter} from "next/navigation";
import React, { useEffect} from "react";
import axios from "axios";

const withAuth = <P extends object>(WrappedComponent: React.ComponentType<P>): React.FC<P> => {
    const ComponentWithAuth = (props: P) => {
        const router = useRouter();

        useEffect(() => {
            const checkAuth = async () => {
                try {
                    const response = await axios.get('http://localhost:8080/api/auth/session', { withCredentials: true });
                    if (response.status !== 200) {
                        router.push('/login');
                    }
                } catch (error) {
                    router.push('/login');
                }
            };

            checkAuth();
        }, [router]);

        return <WrappedComponent {...props} />;
    };

    return ComponentWithAuth;
};

export default withAuth;
