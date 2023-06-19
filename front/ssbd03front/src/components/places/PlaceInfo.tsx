import Box from "@mui/material/Box";
import React, {useEffect, useState} from 'react';
import {Snackbar, Alert} from '@mui/material';
import {ADMIN, API_URL, MANAGER} from "../../consts";
import axios from 'axios';
import Paper from "@mui/material/Paper";
import Typography from "@mui/material/Typography";
import {useTranslation} from "react-i18next";
import {Place} from "../../types/placeInfo";
import {useParams} from "react-router-dom";
import EnterPredictedHotWaterConsumption from "../place/EnterPredictedHotWaterConsumption";
import DoorIcon from "../icons/DoorIcon";
import EditPlace from "../place/EditPlace";
import IconCheckboxMarked from "../icons/IconCheckboxMarked";
import IconCloseBox from "../icons/IconCloseBox";
import ChangePlaceOwner from "../place/ChangePlaceOwner";
import HotWaterEntriesList from "../place/hotWaterEntry/HotWaterEntriesList";

const PlaceInfo = () => {
    const {t} = useTranslation();
    const [currentRole, setCurrentRole] = useState(localStorage.getItem("role"));
    const token = "Bearer " + localStorage.getItem("token");
    const [place, setPlace] = useState<Place | null>(null);
    const [version, setVersion] = useState("");
    const placeId = useParams().placeId;
    const [authorizationErrorOpen, setAuthorizationErrorOpen] = useState(false);
    const [role, setRole] = useState(localStorage.getItem("role"));
    const [confirmOpen, setConfirmOpen] = useState(false);

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

    const handleAuthorizationErrorOpen = () => {
        setAuthorizationErrorOpen(false);
        handleConfirmCancel();
    };

    return (
        <div style={{height: '80vh', overflow: 'hidden'}}>
            <div style={{padding: '3vh', width: '100%', height: '5vh'}}>
                <Box component="form">
                    <Typography variant="h6" sx={{display: 'flex', justifyContent: 'center'}}>
                        <span style={{marginRight: '5vh', marginTop: '1vh'}}><DoorIcon/></span>
                        <span style={{marginRight: '5vh', marginTop: '1vh'}}>{t('place.title')}</span>
                        <span >{role === MANAGER && <EditPlace/>}</span>
                    </Typography>
                </Box>
            </div>
            <div style={{display: 'flex', justifyContent: 'center', maxHeight: '88%'}}>
                    <Box sx={{display: 'flex', flexDirection: 'column', alignItems: 'left'}}>
                        {place !== null && (
                            <>
                                <Paper elevation={3} style={{position: 'relative', overflow: 'auto'}}>
                                    <Typography sx={{
                                        padding: '1vh',
                                        display: 'flex',
                                        alignItems: 'center',
                                        justifyContent: 'space-between'
                                    }} variant="h6">
                                        <b>{t('balances.personal_data')}:</b>
                                    </Typography>
                                    <Typography sx={{
                                        marginLeft: '4vw',
                                        marginRight: '2vw',
                                        padding: '1vh',
                                        display: 'flex',
                                        alignItems: 'center',
                                        justifyContent: 'space-between'
                                    }} variant="h6">
                                        <b>{t('place.firstName')}:</b> {place.firstName}
                                    </Typography>
                                    <Typography sx={{
                                        marginLeft: '4vw',
                                        marginRight: '2vw',
                                        padding: '1vh',
                                        display: 'flex',
                                        alignItems: 'center',
                                        justifyContent: 'space-between'
                                    }} variant="h6">
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
                                    <b>{t('place.hotWaterConnection')}:</b> {place.hotWaterConnection ? <IconCheckboxMarked/> : <IconCloseBox/>}
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
                                    <b>{t('place.centralHeatingConnection')}:</b> {place.centralHeatingConnection ? <IconCheckboxMarked/> : <IconCloseBox/>}
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
                {place?.hotWaterConnection && <Box sx={{margin: '2vh'}}><HotWaterEntriesList/></Box>}
            </Paper>
            <Snackbar open={authorizationErrorOpen} autoHideDuration={6000} onClose={handleAuthorizationErrorOpen}>
                <Alert onClose={handleAuthorizationErrorOpen} severity="error" sx={{ width: '100%' }}>
                    {t('personal_data.authorization_error')}
                </Alert>
            </Snackbar>
                                    <Typography sx={{
                                        marginLeft: '4vw',
                                        marginRight: '2vw',
                                        padding: '1vh',
                                        display: 'flex',
                                        alignItems: 'center',
                                        justifyContent: 'space-between'
                                    }} variant="h6">
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
                                    }} variant="h6">
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
                                    }} variant="h6">
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
                                    }} variant="h6">
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
                                    }} variant="h6">
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
                                    }} variant="h6">
                                        <b>{t('place.hotWaterConnection')}:</b> {place.hotWaterConnection ?
                                        <IconCheckboxMarked/> : <IconCloseBox/>}
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
                                    }} variant="h6">
                                        <b>{t('place.centralHeatingConnection')}:</b> {place.centralHeatingConnection ?
                                        <IconCheckboxMarked/> : <IconCloseBox/>}
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
                                    }} variant="h6">
                                        <b>{t('place.predictedHotWaterConsumption')}:</b> {place.predictedHotWaterConsumption} m³
                                        <EnterPredictedHotWaterConsumption/>
                                    </Typography>
                                </Paper>
                            </>
                        )}
                    </Box>
                <div>
                    {place?.hotWaterConnection &&
                        <Box sx={{maxHeight: '30vh'}}>
                            <HotWaterEntriesList/>
                        </Box>}
                </div>

            </div>

            <Dialog disableEscapeKeyDown open={authorizationErrorOpen}>
                <DialogTitle>{t('personal_data.authorization_error')}</DialogTitle>
                <Button onClick={handleAuthorizationErrorOpen}>{t('confirm.ok')}</Button>
            </Dialog>
        </div>
    );
}
export default PlaceInfo;