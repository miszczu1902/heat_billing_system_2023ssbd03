import {createBrowserRouter, Outlet} from "react-router-dom";
import NavbarPanel from "../components/navigation/NavbarPanel";
import EditPersonalData from "../components/personalData/EditPersonalData";
import Login from "../components/login/Login";
import EnableAccount from "../components/accounts/EnableAccount";
import DisableAccount from "../components/accounts/DisableAccount";
import EditUserPersonalData from "../components/personalData/EditUserPersonalData";

const router = createBrowserRouter([
    {
        path: "/",
        element: (
            <>
                <NavbarPanel/>
                <Outlet/>
            </>
        ),
        children: [
            {
                path: "/#",
            },

            {
                path: "/accounts",
            },
            {
                path: "/accounts/self",
            },
            {
                path: "/accounts/self/personal-data",
                element: <EditPersonalData/>
            },
            {
                path: "/accounts/:username/enable",
                element: <EnableAccount/>
            },
            {
                path: "/accounts/:username/disable",
                element: <DisableAccount/>
            },
            {
                path: '/login',
                element: <Login/>
            },
            {
                path: "/accounts/:username/personal-data",
                element: <EditUserPersonalData/>
            }
        ]
    }
]);

export default router;