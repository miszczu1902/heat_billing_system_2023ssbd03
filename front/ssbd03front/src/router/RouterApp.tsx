import {createBrowserRouter, Outlet, useParams} from "react-router-dom";
import NavbarPanel from "../components/navigation/NavbarPanel";
import EditPersonalData from "../components/personalData/EditPersonalData";
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
                path: "/accounts/:username/personal-data",
                element: <EditUserPersonalData/>
            }
        ]
    }
]);

export default router;