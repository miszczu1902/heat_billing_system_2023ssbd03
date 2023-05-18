import Box from "@mui/material/Box";
import React, {useEffect, useState} from 'react';
import {Grid} from '@mui/material';
import {useNavigate} from "react-router-dom";
import {API_URL} from "../../consts";
import {useCookies} from "react-cookie";
import axios from 'axios';
import Paper from "@mui/material/Paper";
import Typography from "@mui/material/Typography";
import {useTranslation} from "react-i18next";
import EditPersonalData from "../personalData/EditPersonalData";
import jwt from "jwt-decode";
import EditPassword from "../passwords/EditPassword";
import EditEmail from "../email/EditEmail";
import {Owner} from "../../types/owner";
import {Manager} from "../../types/manager";
import {Admin} from "../../types/admin";
import {getRoles} from "@testing-library/react";
import ChangePhoneNumber from "../owner/ChangePhoneNumber";

const OwnerProfile = () => {
    const {t} = useTranslation();
    const navigate = useNavigate();
    const [cookies, setCookie, removeCookie] = useCookies(["token"]);
    const token = "Bearer " + cookies.token;
    const [etag, setEtag] = React.useState(false);
    const [version, setVersion] = React.useState("");
    const [role, setRole] = React.useState('');
    const [owner, setOwner] = useState<Owner | null>(null);


    const UserIcon = ({width = 24, height = 24}) => (
        <svg xmlns="http://www.w3.org/2000/svg" width="80" height="80" fill="currentColor" className="bi bi-person-fill"
             viewBox="0 0 16 16">
            <path d="M3 14s-1 0-1-1 1-4 6-4 6 3 6 4-1 1-1 1H3Zm5-6a3 3 0 1 0 0-6 3 3 0 0 0 0 6Z"/>
        </svg>
    )

    const fetchData = async () => {
        axios.get(`${API_URL}/accounts/self/owner`, {
            headers: {
                'Authorization': token
            }
        }).then(response => {
            setOwner(response.data);
        }).catch(error => {
            if (error.response.status == 403) navigate('/');
        });
    };


    useEffect(() => {
        if (cookies.token !== "undefined" && cookies.token !== undefined) {
            const decodedToken = jwt(cookies.token);
            const decodedRole = JSON.parse(JSON.stringify(decodedToken)).role;
            setRole(decodedRole.split(','));
            const currentTimestamp = Math.floor(new Date().getTime() / 1000);
            if (JSON.parse(JSON.stringify(decodedToken)).exp < currentTimestamp) {
                removeCookie('token');
                navigate('/');
            }
            fetchData();

        } else {
            navigate('/');
        }
    }, [cookies.token]);

    return (
        <div style={{height: '90.3vh', width: '100vw', boxSizing: 'border-box', left: 0, right: 0, bottom: 0}}>
            <Grid container justifyContent="center" alignItems="center"
                  sx={{background: '#1c8de4', height: '100%', width: '100%'}}>
                <Grid my={2} item sm={8} md={5} component={Paper} elevation={6}>
                    <Box component="form" sx={{
                        display: 'flex',
                        flexWrap: 'wrap',
                        alignItems: 'center',
                        justifyContent: 'center',
                        margin: '2vh'
                    }}>
                        <Typography sx={{padding: '1vh'}} variant="h4">{t('profile.title')}</Typography>
                        <UserIcon/>
                    </Box>
                    <Box sx={{my: 30, display: 'flex', flexDirection: 'column', alignItems: 'left', margin: '2vh'}}>
                        {owner !== null && (
                            <>
                            <Paper elevation={3} style={{position: 'relative'}}>
                                <div style={{position: 'absolute', top: '1vh', right: '1vh'}}>
                                    <EditPersonalData/>
                                </div>
                                <Typography sx={{padding: '1vh'}} variant="h5">
                                    <b>{t('personal_data.name')}:</b> {owner.firstName}
                                </Typography>
                                <Typography sx={{padding: '1vh'}} variant="h5">
                                    <b>{t('personal_data.surname')}:</b> {owner.surname}
                                </Typography>
                            </Paper>
                            <Paper elevation={3} style={{position: 'relative'}}>
                                <Typography sx={{padding: '1vh'}}
                                            variant="h5"><b>{t('login.username')}:</b> {owner.username}
                                </Typography>
                            </Paper>
                            <Paper elevation={3} style={{position: 'relative'}}>
                                <div style={{position: 'absolute', top: '1vh', right: '1vh'}}>
                                    <EditEmail/>
                                </div>
                                <Typography sx={{padding: '1vh'}}
                                            variant="h5"><b>{t('register.email')}:</b> {owner.email}</Typography>
                            </Paper>
                            <Paper elevation={3} style={{position: 'relative'}}>
                                <div style={{
                                    position: 'absolute',
                                    top: '1vh',
                                    right: '1vh',
                                    display: 'flex',
                                    gap: '0.5vh'
                                }}>
                                    <ChangePhoneNumber/>
                                </div>
                                <Typography sx={{padding: '1vh'}}
                                            variant="h5"><b>{t('register.phone_number')}:</b> {owner.phoneNumber}
                                </Typography>
                            </Paper>
                            <Paper elevation={3} style={{position: 'relative'}}>
                                <div style={{
                                    position: 'absolute',
                                    top: '1vh',
                                    right: '1vh',
                                    display: 'flex',
                                    gap: '0.5vh'
                                }}>
                                    <EditPassword/>
                                </div>
                                <Typography sx={{padding: '1vh'}}
                                            variant="h5"><b>{t('profile.user_password')}{owner.username}</b>
                                </Typography>
                            </Paper>
                            </>
                            )}
                    </Box>
                </Grid>
            </Grid>
        </div>
    );
}
export default OwnerProfile;