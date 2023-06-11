import validator from "validator";
import { useTranslation } from "react-i18next";
import { useNavigate, useParams } from "react-router-dom";
import { ButtonGroup, Button, Dialog, DialogTitle, DialogContent, DialogContentText, TextField, DialogActions, Snackbar, SnackbarContent, Switch, FormGroup, FormControlLabel} from '@mui/material';
import React, { useEffect, useState } from 'react';
import axios from "axios";
import { API_URL } from '../../consts';
import { set } from "react-hook-form";
import { use } from "i18next";
const EditPlace = () => {
    const token = 'Bearer ' + localStorage.getItem("token");
    const { t, i18n } = useTranslation();
    const navigate = useNavigate();
    const [open, setOpen] = React.useState(false);
    const [confirmOpen, setConfirmOpen] = React.useState(false);
    const placeId = useParams().placeId;
    const [version, setVersion] = useState("");
    const [authorizationErrorOpen, setAuthorizationErrorOpen] = useState(false);
    const [openSnackbar, setOpenSnackbar] = React.useState(false);

    const [area, setArea] = useState("");
    const [areaValid, setAreaValid] = useState(true);
    const [areaError, setAreaError] = useState("");

    const [hotWaterConnection, setWaterConnection] = useState(true);
    const [hotWaterConnectionValid, setWaterConnectionValid] = useState(false);
    const [hotWaterConnectionError, setWaterConnectionError] = useState("");

    const [centralHeatingConnection, setCentralHeatingConnection] = useState(true);
    const [centralHeatingConnectionValid, setCentralHeatingConnectionValid] = useState(false);
    const [centralHeatingConnectionError, setCentralHeatingConnectionError] = useState("");


    const fetchData = async () => {
        axios.get(`${API_URL}/places/place/${placeId}`,{
            headers: {
                'Authorization': token
            }
        }).then((response) => {
            setVersion(response.data.version);
            localStorage.setItem("etag", response.headers["etag"]);
            setArea(response.data.area);
            setWaterConnection(response.data.hotWaterConnection);
            setCentralHeatingConnection(response.data.centralHeatingConnection);
        }).catch((error) => {
            if (error.response.status === 403) {
                setAuthorizationErrorOpen(true);
            }
        });
    }

    useEffect(() => {
        fetchData();
    }, []);

    const handleAreaChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const regex = /^[0-9]+(?:\.[0-9]+)?$/;
        if (validator.matches(event.target.value, regex)) {
            setArea(event.target.value);
            setAreaValid(true);
            setAreaError("");
        } else {
            setAreaError(t("editPlace.edit_place_error"));
            setAreaValid(false);
        }
    };

    const handleWaterConnectionChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setWaterConnection(event.target.checked);
    };

    const handleCentralHeatingConnectionChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setCentralHeatingConnection(event.target.checked);
    };


    const handleClickOpen = () => {
        fetchData();
        setOpen(true);
    };

    const handleClose = () => {
        setAreaError("");
        setAreaValid(false);

        setWaterConnectionError("");
        setWaterConnectionValid(false);

        setCentralHeatingConnectionError("");
        setCentralHeatingConnectionValid(false);

        setOpen(false);
    };

    const handleSave = () => {
        setConfirmOpen(true);
    };

    const handleConfirmCancel = () => {
        setConfirmOpen(false);
    };

    const handleAuthorizationErrorOpen = (event: React.SyntheticEvent<unknown>, reason?: string) => {
        if (reason !== 'backdropClick') {
            setAuthorizationErrorOpen(false);
        }
    };

    const handleConfirmSave = () => {  
        if(!areaValid && !hotWaterConnectionValid && !centralHeatingConnectionValid) {
            setOpenSnackbar(true);
            setConfirmOpen(false);
            return;
        }

        const editPlaceDTO = {
            area : area,
            hotWaterConnection : hotWaterConnection,
            centralHeatingConnection : centralHeatingConnection,
            version: version
        }
        axios.patch(`${API_URL}/places/place/${placeId}`,
        editPlaceDTO, {
            headers: {
                'Authorization': token,
                'If-Match': localStorage.getItem("etag")
            }
        }).then((response) => {
            window.location.reload();
        }).catch((error) => {
            setConfirmOpen(false); 
            setOpenSnackbar(true);
            return;
        });

        setConfirmOpen(false); 
        setOpen(false);
    };

    const handleCloseSnackbar = () => { 
        setOpenSnackbar(false);
    };

    setTimeout(handleCloseSnackbar, 10000);
    


    return (
        <div>
            <div className="row">
                <div className="col-12">
                    <ButtonGroup>
                        <Button variant="contained" color="primary" onClick={handleClickOpen}>
                            {t('editPlace.edit_place_button')}
                        </Button>
                    </ButtonGroup>
                </div>
            </div>


            <Dialog open={open} onClose={handleClose}>
                <DialogTitle>{t('editPlace.edit_place_title')}</DialogTitle>
                <DialogContent>
                <DialogContentText>{t('editPlace.edit_place_description')}</DialogContentText>
                <TextField
                    autoFocus
                    margin="dense"
                    id="name"
                    label={t('editPlace.edit_place_text_field_area')}
                    fullWidth
                    variant="standard"
                    defaultValue={parseFloat(area).toFixed(2)}
                    onChange={handleAreaChange}/>
                    <DialogContentText style={{ fontSize: "13px", color: "red" }}>
                        {areaError}
                    </DialogContentText>
                </DialogContent>
                <DialogContent>
                <DialogContentText>
                </DialogContentText>                    
                     <FormGroup>
                        <FormControlLabel control={<Switch 
                                                    checked={hotWaterConnection}
                                                    onChange={handleWaterConnectionChange}
                                                    inputProps={{ 'aria-label': 'controlled' }} />} label={t('editPlace.edit_place_text_field_hot_water_connection')} />
                        <FormControlLabel control={<Switch 
                                                    checked={centralHeatingConnection}
                                                    onChange={handleCentralHeatingConnectionChange}
                                                    inputProps={{ 'aria-label': 'controlled' }} />} label={t('editPlace.edit_place_text_field_central_heating_connection')} />
                    </FormGroup>
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleSave}>{t('confirm.save')}</Button>
                    <Button onClick={handleClose}>{t('confirm.cancel')}</Button>
                </DialogActions>
            </Dialog>

            <Dialog open={confirmOpen} onClose={handleConfirmCancel}>
                <DialogTitle>{t('editPlace.edit_place_title')}</DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        {t('editPlace.edit_place_confirm')}
                    </DialogContentText>
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleConfirmSave}>{t('confirm.yes')}</Button>
                    <Button onClick={handleConfirmCancel}>{t('confirm.no')}</Button>
                </DialogActions>
            </Dialog>


            <Dialog disableEscapeKeyDown open={authorizationErrorOpen}>
                <DialogTitle>{t('personal_data.authorization_error')}</DialogTitle>
                <Button onClick={handleAuthorizationErrorOpen}>{t('confirm.ok')}</Button>
            </Dialog>

            <Snackbar open={openSnackbar} onClose={handleCloseSnackbar}>
                <SnackbarContent 
                message={t('editPlace.edit_place_failed')}/>
            </Snackbar>
        </div>
    );

}

export default EditPlace;