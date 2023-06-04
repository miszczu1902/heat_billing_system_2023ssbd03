import validator from "validator";
import { useTranslation } from "react-i18next";
import { useNavigate } from "react-router-dom";
import { ButtonGroup, Button, Dialog, DialogTitle, DialogContent, DialogContentText, TextField, DialogActions } from '@mui/material';
import React, { useEffect, useState } from 'react';
import { set } from "react-hook-form";
import axios from "axios";
import { API_URL } from '../../consts';
import e from "express";

const EnterPredictedHotWaterConsumption = () => {
    const token = 'Bearer ' + localStorage.getItem("token");
    const { t, i18n } = useTranslation();
    const navigate = useNavigate();
    const [open, setOpen] = React.useState(false);
    const [confirmOpen, setConfirmOpen] = React.useState(false);
    const [placeId, setPlaceId] = useState("");
    const [version, setVersion] = useState("");

    const [predictedHotWaterConsumption, setPredictedHotWaterConsumption] = useState("");
    const [predictedHotWaterConsumptionValid, setPredictedHotWaterConsumptionValid] = useState(false);
    const [predictedHotWaterConsumptionError, setPredictedHotWaterConsumptionError] = useState("");

    const [authorizationErrorOpen, setAuthorizationErrorOpen] = useState(false);

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
        const enterPredictedHotWaterConsumptionDTO = {
            consumption: predictedHotWaterConsumption,
            version: version
        }
        axios.post(`${API_URL}/place/${placeId}/predicted-hot-water-consumption`,
        enterPredictedHotWaterConsumptionDTO, {
            headers: {
                'Authorization': token,
                'If-Match': localStorage.getItem("etag")
            }
        }).then((response) => {
        }).catch((error) => {
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

    const handleAuthorizationErrorOpen = (event: React.SyntheticEvent<unknown>, reason?: string) => {
        if (reason !== 'backdropClick') {
            setAuthorizationErrorOpen(false);
            handleConfirmCancel();
        }
    };

    return (
        <div className="container">
            <div className="row">
                <div className="col-12">
                    <ButtonGroup>
                        <Button variant="contained" color="primary" onClick={handleClickOpen}>
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
            label={t('enterPredictedHotWaterConsumption.enter_predicted_hot_water_consumption_text_field_name')}
            fullWidth
            variant="standard"
            defaultValue={"0"}
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

        <Dialog disableEscapeKeyDown open={authorizationErrorOpen}>
                <DialogTitle>{t('personal_data.authorization_error')}</DialogTitle>
                <Button onClick={handleAuthorizationErrorOpen}>{t('confirm.ok')}</Button>
        </Dialog>
      </div>
    );
}

export default EnterPredictedHotWaterConsumption;