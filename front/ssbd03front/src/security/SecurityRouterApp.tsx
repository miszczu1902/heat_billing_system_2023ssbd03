import {createBrowserRouter, Outlet, useNavigate} from 'react-router-dom';
import NavbarPanel from '../components/navigation/NavbarPanel';
import EditPersonalData from '../components/personalData/EditPersonalData';
import Login from '../components/login/Login';
import EnableAccount from '../components/accounts/EnableAccount';
import DisableAccount from '../components/accounts/DisableAccount';
import EditUserPersonalData from '../components/personalData/EditUserPersonalData';
import LandingPage from '../components/landingPage/LandingPage';
import AccountsList from '../components/accountsList/AccountsList';
import Registration from "../components/registration/Registration";
import EditPassword from "../components/passwords/EditPassword";
import ResetPassword from "../components/passwords/ResetPassword";
import ActivateFromEmail from "../components/registration/ActivateFromEmail";
import ChangePhoneNumber from "../components/owner/ChangePhoneNumber";
import EditUserPassword from "../components/passwords/EditUserPassword";
import Profile from "../components/account/Profile";
import EditEmail from "../components/email/EditEmail";
import ConfirmEmail from "../components/email/ConfirmEmail";
import EditUserEmail from "../components/email/EditUserEmail";
import OwnerProfile from "../components/account/OwnerProfile";
import ManagerProfile from "../components/account/ManagerProfile";
import AdminProfile from "../components/account/AdminProfile";
import {useCookies} from "react-cookie";
import {ADMIN, GUEST, MANAGER, OWNER} from "../consts";
import {useEffect} from "react";
import NotFoundPage from "../components/notFound/NotFoundPage";

interface PrivateRouteProps {
    component: React.ComponentType<any>;
    accessLevels: string[];
}

const PrivateRoute: React.FC<PrivateRouteProps> = ({component: Component, accessLevels, ...rest}) => {
    const [cookies, setCookie] = useCookies(['token', 'role']);
    const userAccessLevel = cookies.role;
    const navigate = useNavigate();

    useEffect(() => {
        if (cookies.token === "undefined" || cookies.token === undefined) {
            setCookie("role", GUEST);
        }
        if (!accessLevels.includes(userAccessLevel)) {
            navigate('/');
            window.location.reload();
        }
    }, [cookies.role]);


    return <Component {...rest} />;
};

const router = createBrowserRouter([
    {
        path: '/',
        element: (<><Outlet/></>),
        children: [
            {
                path: '/',
                element: <PrivateRoute component={LandingPage} accessLevels={[ADMIN, MANAGER, OWNER, GUEST]}/>
            },
            {
                path: '*',
                element: <PrivateRoute component={NotFoundPage} accessLevels={[ADMIN, MANAGER, OWNER, GUEST]}/>
            },
            {
                path: '*',
                element: <NotFoundPage/>
            },
            {
                path: '/accounts',
                element: (<><NavbarPanel/><Outlet/></>),
                children: [
                    {
                        path: '/accounts',
                        element: <PrivateRoute component={AccountsList} accessLevels={[ADMIN, MANAGER]}/>
                    },
                    {
                        path: '/accounts/self/owner',
                        element: <PrivateRoute component={OwnerProfile} accessLevels={[OWNER]}/>
                    },
                    {
                        path: '/accounts/self/manager',
                        element: <PrivateRoute component={ManagerProfile} accessLevels={[MANAGER]}/>
                    },
                    {
                        path: '/accounts/self/admin',
                        element: <PrivateRoute component={AdminProfile} accessLevels={[ADMIN]}/>
                    },
                    {
                        path: '/accounts/:username',
                        element: <PrivateRoute component={Profile} accessLevels={[ADMIN, MANAGER, OWNER]}/>
                    },
                    {
                        path: '/accounts/self/personal-data',
                        element: <PrivateRoute component={EditPersonalData} accessLevels={[ADMIN, MANAGER, OWNER]}/>
                    },
                    {
                        path: '/accounts/:username/personal-data',
                        element: <PrivateRoute component={EditUserPersonalData} accessLevels={[ADMIN, MANAGER]}/>
                    },
                    {
                        path: '/accounts/:username/email',
                        element: <PrivateRoute component={EditUserEmail} accessLevels={[ADMIN, MANAGER]}/>
                    },
                    {
                        path: '/accounts/:username/enable',
                        element: <PrivateRoute component={EnableAccount} accessLevels={[ADMIN]}/>
                    },
                    {
                        path: '/accounts/:username/disable',
                        element: <PrivateRoute component={DisableAccount} accessLevels={[ADMIN]}/>
                    },
                ]
            },
            {
                path: "/accounts/self/password",
                element: <PrivateRoute component={EditPassword} accessLevels={[ADMIN, MANAGER, OWNER]}/>
            },
            {
                path: "/accounts/reset-password",
                element: <PrivateRoute component={ResetPassword} accessLevels={[GUEST]}/>
            },
            {
                path: "/accounts/:username/password",
                element: <PrivateRoute component={EditUserPassword} accessLevels={[ADMIN, MANAGER]}/>

            },
            {
                path: '/login',
                element: <PrivateRoute component={Login} accessLevels={[GUEST]}/>
            },
            {
                path: '/register',
                element: <PrivateRoute component={Registration} accessLevels={[GUEST]}/>

            },
            {
                path: '/activate-from-email/:activationToken',
                element: <PrivateRoute component={ActivateFromEmail} accessLevels={[GUEST]}/>
            },
            {
                path: "/accounts/self/phone-number",
                element: <PrivateRoute component={ChangePhoneNumber} accessLevels={[OWNER]}/>
            },
            {
                path: '/accounts/self/email',
                element: <PrivateRoute component={EditEmail} accessLevels={[ADMIN, MANAGER, OWNER]}/>
            },
            {
                path: '/accounts/self/confirm-new-email/:activationToken',
                element: <PrivateRoute component={ConfirmEmail} accessLevels={[ADMIN, MANAGER, OWNER, GUEST]}/>
            }
        ]
    }
]);
export default router;