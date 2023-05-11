import {createBrowserRouter, Outlet} from "react-router-dom";
import EditUserPersonalData from "../components/personalData/EditUserPersonalData";
import NavbarPanel from "../components/navigation/NavbarPanel";
import AccountsList from "../components/accountsList/AccountsList";
import EditPersonalData from "../components/personalData/EditPersonalData";
import LandingPage from "../components/landingPage/LandingPage";

const router = createBrowserRouter([
    {
        path: '/',
        element: <LandingPage />
    },
    {
        path: "/accounts",
        element: (
            <>
                <NavbarPanel/>
                <Outlet/>
            </>
        ),
        children: [
            {
                path: "/accounts",
                element: <AccountsList/>
            },
            {
                path: "/accounts/:username",
                element: <EditUserPersonalData/>
            }
        ]
    },
    {
        path: '/self',
        element: <EditPersonalData/>
    }
]);

export default router;