import validator from "validator";
import { useTranslation } from "react-i18next";
import { useNavigate, useParams } from "react-router-dom";
import { ButtonGroup, Button, Dialog, DialogTitle, DialogContent, DialogContentText, TextField, DialogActions, Snackbar, Alert } from '@mui/material';
import React, { useEffect, useState } from 'react';
import axios from "axios";
import { API_URL } from '../../consts';

const EnterPredictedHotWaterConsumption = () => {
    const token = 'Bearer ' + localStorage.getItem("token");
    const { t, i18n } = useTranslation();
    const navigate = useNavigate();
    const [open, setOpen] = React.useState(false);
    const [confirmOpen, setConfirmOpen] = React.useState(false);
    const placeId = useParams().placeId;
    const [version, setVersion] = useState("");

    const [predictedHotWaterConsumption, setPredictedHotWaterConsumption] = useState("");
    const [predictedHotWaterConsumptionValid, setPredictedHotWaterConsumptionValid] = useState(false);
    const [predictedHotWaterConsumptionError, setPredictedHotWaterConsumptionError] = useState("");

    const [authorizationErrorOpen, setAuthorizationErrorOpen] = useState(false);
    const [openSnackbar, setOpenSnackbar] = React.useState(false);

    useEffect(() => {
        fetchData();
    }, []);

    const fetchData = async () => {
        axios.get(`${API_URL}/places/place/${placeId}`,{
            headers: {
                'Authorization': token
            }
        }).then((response) => {
            setVersion(response.data.version);
            localStorage.setItem("etag", response.headers["etag"]);
            setPredictedHotWaterConsumption(response.data.predictedHotWaterConsumption);
        }).catch((error) => {
            if (error.response.status === 403) {
                setAuthorizationErrorOpen(true);
            }
        });
    }

    const handleClickOpen = () => {
        setOpen(true);
    };

    const handleClose = () => {
        setPredictedHotWaterConsumptionError("");
        setPredictedHotWaterConsumptionValid(false);
        setOpen(false);
    };

    const handleSave = () => {
        setConfirmOpen(true);
    };

    const handleConfirmSave = () => {  
        if(!predictedHotWaterConsumptionValid) {
            setOpenSnackbar(true);
            setConfirmOpen(false);
            return;
        }

        const enterPredictedHotWaterConsumptionDTO = {
            consumption: predictedHotWaterConsumption,
            version: version
        }
        axios.patch(`${API_URL}/places/place/${placeId}/predicted-hot-water-consumption`,
        enterPredictedHotWaterConsumptionDTO, {
            headers: {
                'Authorization': token,
                'If-Match': localStorage.getItem("etag")
            }
        }).then((response) => {
            //window.location.reload()();
        }).catch((error) => {
            setConfirmOpen(false); 
            setOpenSnackbar(true);
            return;
        });

        setConfirmOpen(false); 
        setOpen(false);
    };

    const handleConfirmCancel = () => {
        setConfirmOpen(false);
    };

    const handlePredictedHotWaterConsumptionChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const regex = /^[0-9]+(?:\.[0-9]+)?$/;
        if (validator.matches(event.target.value, regex)) {
            setPredictedHotWaterConsumption(event.target.value);
            setPredictedHotWaterConsumptionValid(true);
            setPredictedHotWaterConsumptionError("");
        } else {
            setPredictedHotWaterConsumptionValid(false);
            setPredictedHotWaterConsumptionError(t('enterPredictedHotWaterConsumption.enter_predicted_hot_water_consumption_error'));
        }
    }

    const handleAuthorizationErrorOpen = () => {
        setAuthorizationErrorOpen(false);
        handleConfirmCancel();
    };
    
    const handleCloseSnackbar = () => { 
        setOpenSnackbar(false);
    };

    return (
        <div className="container" style={{maxWidth: '40%'}}>
            <div className="row">
                <div className="col-12">
                    <ButtonGroup>
                        <Button variant="contained" color="primary" onClick={handleClickOpen} style={{wordWrap: 'break-word'}}>
                            {t('enterPredictedHotWaterConsumption.enter_predicted_hot_water_consumption_button')}
                        </Button>
                    </ButtonGroup>
                </div>
            </div>

        <Dialog open={open} onClose={handleClose}>
        <DialogTitle>{t('enterPredictedHotWaterConsumption.enter_predicted_hot_water_consumption_title')}</DialogTitle>
        <DialogContent>
          <DialogContentText>
          {t('enterPredictedHotWaterConsumption.enter_predicted_hot_water_consumption_description')}
          </DialogContentText>
          <TextField
            autoFocus
            margin="dense"
            id="name"
            label={t('enterPredictedHotWaterConsumption.enter_predicted_hot_water_consumption_text_field_name')+"*"}
            fullWidth
            variant="standard"
            defaultValue={parseFloat(predictedHotWaterConsumption).toFixed(2)}
            onChange={handlePredictedHotWaterConsumptionChange}
          />
            <DialogContentText style={{ fontSize: "13px", color: "red" }}>
                {predictedHotWaterConsumptionError}
            </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleSave}>{t('confirm.save')}</Button>
          <Button onClick={handleClose}>{t('confirm.cancel')}</Button>
        </DialogActions>
      </Dialog>

        <Dialog open={confirmOpen} onClose={handleConfirmCancel}>
        <DialogTitle>{t('enterPredictedHotWaterConsumption.enter_predicted_hot_water_consumption_title')}</DialogTitle>
        <DialogContent>
            <DialogContentText>
                {t('enterPredictedHotWaterConsumption.enter_predicted_hot_water_consumption_confirm')}
            </DialogContentText>
        </DialogContent>
        <DialogActions>
            <Button onClick={handleConfirmSave}>{t('confirm.yes')}</Button>
            <Button onClick={handleConfirmCancel}>{t('confirm.no')}</Button>
        </DialogActions>
        </Dialog>

        <Snackbar open={authorizationErrorOpen} autoHideDuration={6000} onClose={handleAuthorizationErrorOpen}>
            <Alert onClose={handleAuthorizationErrorOpen} severity="error">
                {t('personal_data.authorization_error')}
            </Alert>
        </Snackbar>

        <Snackbar open={openSnackbar} autoHideDuration={6000} onClose={handleCloseSnackbar}>
            <Alert onClose={handleCloseSnackbar} severity="error">
                {t('enterPredictedHotWaterConsumption.enter_predicted_hot_water_consumption_failed')}
            </Alert>
        </Snackbar>
      </div>
    );
}

export default EnterPredictedHotWaterConsumption;