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
import EditPassword from "../passwords/EditPassword";
import EditEmail from "../email/EditEmail";
import {Manager} from "../../types/manager";
import UserIcon from "../icons/UserIcon";

const OwnerProfile = () => {
    const {t} = useTranslation();
    const navigate = useNavigate();
    const [cookies, setCookie, removeCookie] = useCookies(["token"]);
    const token = "Bearer " + cookies.token;
    const [etag, setEtag] = useState(false);
    const [version, setVersion] = useState("");
    const [role, setRole] = useState('');
    const [manager, setManager] = useState<Manager | null>(null);

    const fetchData = async () => {
        axios.get(`${API_URL}/accounts/self/manager`, {
            headers: {
                'Authorization': token
            }
        }).then(response => {
            setManager(response.data);
        }).catch(error => {
            if (error.response.status == 403) navigate('/');
        });
    };

    useEffect(() => {
        fetchData();
    });

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
                        {manager !== null && (
                            <>
                                <Paper elevation={3} style={{position: 'relative'}}>
                                    <div style={{position: 'absolute', top: '1vh', right: '1vh'}}>
                                        <EditPersonalData/>
                                    </div>
                                    <Typography sx={{padding: '1vh'}} variant="h5">
                                        <b>{t('personal_data.name')}:</b> {manager.firstName}
                                    </Typography>
                                    <Typography sx={{padding: '1vh'}} variant="h5">
                                        <b>{t('personal_data.surname')}:</b> {manager.surname}
                                    </Typography>
                                </Paper>
                                <Paper elevation={3} style={{position: 'relative'}}>
                                    <Typography sx={{padding: '1vh'}}
                                                variant="h5"><b>{t('login.username')}:</b> {manager.username}
                                    </Typography>
                                </Paper>
                                <Paper elevation={3} style={{position: 'relative'}}>
                                    <div style={{position: 'absolute', top: '1vh', right: '1vh'}}>
                                        <EditEmail/>
                                    </div>
                                    <Typography sx={{padding: '1vh'}}
                                                variant="h5"><b>{t('register.email')}:</b> {manager.email}</Typography>
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
                                                variant="h5"><b>{t('profile.user_password')}{manager.username}</b>
                                    </Typography>
                                </Paper>
                                <Paper elevation={3} style={{position: 'relative'}}>
                                    <Typography sx={{padding: '1vh'}}
                                                variant="h5"><b>{t('profile.license')}:</b> {manager.license}
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