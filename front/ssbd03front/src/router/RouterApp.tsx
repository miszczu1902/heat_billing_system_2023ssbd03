import {createBrowserRouter, Outlet} from 'react-router-dom';
import NavbarPanel from '../components/navigation/NavbarPanel';
import EditPersonalData from '../components/personalData/EditPersonalData';
import Login from '../components/login/Login';
import EnableAccount from '../components/accounts/EnableAccount';
import DisableAccount from '../components/accounts/DisableAccount';
import EditUserPersonalData from '../components/personalData/EditUserPersonalData';
import LandingPage from '../components/landingPage/LandingPage';
import AccountsList from '../components/accountsList/AccountsList';

const router = createBrowserRouter([
    {
        path: '/',
        element: (<><Outlet/></>),
        children: [
            {
                path: '/',
                element: <LandingPage/>
            },
            {
                path: '/accounts',
                element: (<><NavbarPanel/><Outlet/></>),
                children: [
                    {
                        path: '/accounts',
                        element: <AccountsList/>
                    },
                    {
                        path: '/accounts/self',
                    },
                    {
                        path: '/accounts/:username'
                    },
                    {
                        path: '/accounts/self/personal-data',
                        element: <EditPersonalData/>
                    },
                    {
                        path: '/accounts/:username/personal-data',
                        element: <EditUserPersonalData/>
                    },
                    {
                        path: '/accounts/:username/enable',
                        element: <EnableAccount/>
                    },
                    {
                        path: '/accounts/:username/disable',
                        element: <DisableAccount/>
                    },
                ]
            },
            {
                path: '/login',
                element: <Login/>
            },
            {
                path: '/register'
            }
        ]
    }
]);
export default router;