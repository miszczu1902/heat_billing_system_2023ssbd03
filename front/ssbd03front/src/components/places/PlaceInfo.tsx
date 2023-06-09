import Box from "@mui/material/Box";
import React, {useEffect, useState} from 'react';
import {Button, Dialog, DialogTitle, Grid} from '@mui/material';
import {ADMIN, API_URL, MANAGER} from "../../consts";
import axios from 'axios';
import Paper from "@mui/material/Paper";
import Typography from "@mui/material/Typography";
import {useTranslation} from "react-i18next";
import {Place} from "../../types/placeInfo";
import {useParams} from "react-router-dom";
import EnterPredictedHotWaterConsumption from "../place/EnterPredictedHotWaterConsumption";
import DoorIcon from "../icons/DoorIcon";
import {Account} from "../../types/account";
import ChangePlaceOwner from "../place/ChangePlaceOwner";

const PlaceInfo = () => {
    const {t} = useTranslation();
    const [currentRole, setCurrentRole] = useState(localStorage.getItem("role"));
    const token = "Bearer " + localStorage.getItem("token");
    const [place, setPlace] = useState<Place | null>(null);
    const [version, setVersion] = useState("");
    const placeId = useParams().placeId;
    const [authorizationErrorOpen, setAuthorizationErrorOpen] = useState(false);
    const [confirmOpen, setConfirmOpen] = React.useState(false);

    useEffect(() => {
        fetchData();
    }, [placeId]);

    const fetchData = async () => {
        axios.get(`${API_URL}/places/place/${placeId}`, {
            headers: {
                'Authorization': token
            }
        }).then((response) => {
            setVersion(response.data.version);
            setPlace(response.data);
            localStorage.setItem("etag", response.headers["etag"]);
        }).catch((error) => {
            if (error.response.status === 403) {
                setAuthorizationErrorOpen(true);
            }
        });
    }

    const handleConfirmCancel = () => {
        setConfirmOpen(false);
    };

    const handleAuthorizationErrorOpen = (event: React.SyntheticEvent<unknown>, reason?: string) => {
        if (reason !== 'backdropClick') {
            setAuthorizationErrorOpen(false);
            handleConfirmCancel();
        }
    };


    return (
        <div style={{height: '93.3vh', width: '100vw', boxSizing: 'border-box', left: 0, right: 0, bottom: 0}}>
            <Paper elevation={3} style={{position: 'relative', overflow: 'auto'}}>
                <Box component="form" sx={{
                    display: 'flex',
                    flexWrap: 'wrap',
                    alignItems: 'center',
                    justifyContent: 'center',
                    margin: '2vh'
                }}>        <Typography sx={{ padding: '1vh' }} variant="h4">
                            {t('place.title')}
                        </Typography>
                        <DoorIcon />
                    </Box>
                    <Box sx={{ my: 30, display: 'flex', flexDirection: 'column', alignItems: 'left', margin: '2vh' }}>
                        {place !== null && (
                            <>
                                <Paper elevation={3} style={{ position: 'relative', overflow: 'auto' }}>
                                    <Typography sx={{ padding: '1vh', display: 'flex', alignItems: 'center', justifyContent: 'space-between' }} variant="h4">
                                        <b>{t('balances.personal_data')}:</b>
                                    </Typography>
                                    <Typography sx={{ marginLeft: '4vw', marginRight: '2vw', padding: '1vh', display: 'flex', alignItems: 'center', justifyContent: 'space-between' }} variant="h5">
                                        <b>{t('place.firstName')}:</b> {place.firstName}
                                    </Typography>
                                    <Typography sx={{ marginLeft: '4vw', marginRight: '2vw', padding: '1vh', display: 'flex', alignItems: 'center', justifyContent: 'space-between' }} variant="h5">
                                        <b>{t('place.surname')}:</b> {place.surname}

                                    </Typography>
                                <Typography sx={{
                                    marginLeft: '4vw',
                                    marginRight: '2vw',
                                    padding: '1vh',
                                    display: 'flex',
                                    alignItems: 'center',
                                    justifyContent: 'space-between'
                                }} variant="h5">
                                    <b>{t('place.username')}:</b> {place.username}
                                    {
                                        (currentRole === MANAGER) && <ChangePlaceOwner/>
                                    }
                                </Typography>
                            </Paper>
                            <Paper elevation={3} style={{marginTop: '0.5vh', position: 'relative', overflow: 'auto'}}>
                                <Typography sx={{
                                    marginLeft: '2vw',
                                    marginRight: '2vw',
                                    padding: '1vh',
                                    display: 'flex',
                                    alignItems: 'center',
                                    justifyContent: 'space-between'
                                }} variant="h5">
                                    <b>{t('place.created_by')}:</b> {place.createdBy}
                                </Typography>
                            </Paper>
                            <Paper elevation={3} style={{marginTop: '0.5vh', position: 'relative', overflow: 'auto'}}>
                                <Typography sx={{
                                    marginLeft: '2vw',
                                    marginRight: '2vw',
                                    padding: '1vh',
                                    display: 'flex',
                                    alignItems: 'center',
                                    justifyContent: 'space-between'
                                }} variant="h5">
                                    <b>{t('place.id')}:</b> {place.id}
                                </Typography>
                            </Paper>
                            <Paper elevation={3} style={{marginTop: '0.5vh', position: 'relative', overflow: 'auto'}}>
                                <Typography sx={{
                                    marginLeft: '2vw',
                                    marginRight: '2vw',
                                    padding: '1vh',
                                    display: 'flex',
                                    alignItems: 'center',
                                    justifyContent: 'space-between'
                                }} variant="h5">
                                    <b>{t('place.placeNumber')}:</b> {place.placeNumber}
                                </Typography>
                            </Paper>
                            <Paper elevation={3} style={{marginTop: '0.5vh', position: 'relative', overflow: 'auto'}}>
                                <Typography sx={{
                                    marginLeft: '2vw',
                                    marginRight: '2vw',
                                    padding: '1vh',
                                    display: 'flex',
                                    alignItems: 'center',
                                    justifyContent: 'space-between'
                                }} variant="h5">
                                    <b>{t('place.area')}:</b> {place.area} m²
                                </Typography>
                            </Paper>
                            <Paper elevation={3} style={{marginTop: '0.5vh', position: 'relative', overflow: 'auto'}}>
                                <Typography sx={{
                                    marginLeft: '2vw',
                                    marginRight: '2vw',
                                    padding: '1vh',
                                    display: 'flex',
                                    alignItems: 'center',
                                    justifyContent: 'space-between'
                                }} variant="h5">
                                    <b>{t('place.hotWaterConnection')}:</b> {place.hotWaterConnection ? t('place.connected') : t('place.disconnected')}
                                </Typography>
                            </Paper>
                            <Paper elevation={3} style={{marginTop: '0.5vh', position: 'relative', overflow: 'auto'}}>
                                <Typography sx={{
                                    marginLeft: '2vw',
                                    marginRight: '2vw',
                                    padding: '1vh',
                                    display: 'flex',
                                    alignItems: 'center',
                                    justifyContent: 'space-between'
                                }} variant="h5">
                                    <b>{t('place.centralHeatingConnection')}:</b> {place.centralHeatingConnection ? t('place.connected') : t('place.disconnected')}
                                </Typography>
                            </Paper>
                            <Paper elevation={3} style={{marginTop: '0.5vh', position: 'relative', overflow: 'auto'}}>
                                <Typography sx={{
                                    marginLeft: '2vw',
                                    marginRight: '2vw',
                                    padding: '1vh',
                                    display: 'flex',
                                    alignItems: 'center',
                                    justifyContent: 'space-between'
                                }} variant="h5">
                                    <b>{t('place.predictedHotWaterConsumption')}:</b> {place.predictedHotWaterConsumption} m³
                                    <EnterPredictedHotWaterConsumption/>
                                </Typography>
                            </Paper>
                        </>
                    )}
                </Box>
            </Paper>
            <Dialog disableEscapeKeyDown open={authorizationErrorOpen}>
                <DialogTitle>{t('personal_data.authorization_error')}</DialogTitle>
                <Button onClick={handleAuthorizationErrorOpen}>{t('confirm.ok')}</Button>
            </Dialog>
        </div>
    );
}
export default PlaceInfo;