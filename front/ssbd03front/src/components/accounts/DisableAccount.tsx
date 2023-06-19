import * as React from 'react';
import {useState} from 'react';
import Button from '@mui/material/Button';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogTitle from '@mui/material/DialogTitle';
import { Snackbar, Alert } from '@mui/material';
import axios from 'axios';
import {API_URL} from '../../consts';
import {useParams} from "react-router-dom";
import {useTranslation} from "react-i18next";

const DisableAccount = () => {
    const {t, i18n} = useTranslation();
    const username = useParams().username;
    const token = "Bearer " + localStorage.getItem("token");
    const [version, setVersion] = useState("");
    const [enable, setEnable] = useState(false);

    const [open, setOpen] = useState(false);
    const [confirmOpen, setConfirmOpen] = useState(false);

    const [successOpen, setSuccessOpen] = useState(false);
    const [errorOpen, setErrorOpen] = useState(false);

    const [blockedUserOpen, setBlockedUserOpen] = useState(false);

    const [authorizationErrorOpen, setAuthorizationErrorOpen] = useState(false);


    const fetchData = async () => {
        await axios.get(`${API_URL}/accounts/${username}`, {
            headers: {
                Authorization: token
            }
        })
            .then(response => {
                setVersion(response.data.version.toString());
                setEnable(response.data.isEnable);
            })
            .catch(error => {
                if (error.response.status === 403) {
                    setAuthorizationErrorOpen(true);
                    return;
                }
            });
    };

    const disable = async () => {
        axios.patch(`${API_URL}/accounts/${username}/disable`,
            {},
            {
                headers: {
                    'Authorization': token,
                },
            })
            .then(response => {
                setSuccessOpen(true);
            })
            .catch(error => {
                if (error.response.status === 403) {
                    setAuthorizationErrorOpen(true);
                    return;
                }
                setErrorOpen(true);
            });
    };

    const handleClickOpen = () => {
        fetchData();
        setOpen(true);
    };

    const handleConfirmClose = () => {
        setOpen(false);
    }

    const handleConfirmConfirm = (event: React.SyntheticEvent<unknown>, reason?: string) => {
        if (reason !== 'backdropClick') {
            setConfirmOpen(false);
        }
        if (!enable) {
            setBlockedUserOpen(true);
            return;
        }
        disable();
        handleConfirmClose();
    }

    const handleSuccessClose = () => {
        setSuccessOpen(false);
        window.location.reload();
    }

    const handleErrorClose = () => {
        setErrorOpen(false);
    };

    const handleBlockedUserOpen = () => {
        setBlockedUserOpen(false);
        handleConfirmClose();
    };

    const handleAuthorizationErrorOpen = () => {
        setAuthorizationErrorOpen(false);
        handleConfirmClose();
    };



    return (
        <div>
            <div>
                <Button onClick={handleClickOpen} variant="contained">{t('disable_account.disable')}</Button>
            </div>
            <Dialog disableEscapeKeyDown open={open}>
                <DialogTitle>{t('disable_account.disable_confirm')}{username}?</DialogTitle>
                <DialogActions>
                    <Button onClick={handleConfirmClose}>{t('confirm.no')}</Button>
                    <Button onClick={handleConfirmConfirm}>{t('confirm.yes')}</Button>
                </DialogActions>
            </Dialog>

            <Snackbar open={successOpen} autoHideDuration={6000} onClose={handleSuccessClose}>
                <Alert onClose={handleSuccessClose} severity="success" sx={{width: '100%'}}>
                    {t('disable_account.success_one')}{username}{t('disable_account.success_two')}
                </Alert>
            </Snackbar>

            <Snackbar open={errorOpen} autoHideDuration={6000} onClose={handleErrorClose}>
                <Alert onClose={handleErrorClose} severity="error" sx={{width: '100%'}}>
                    {t('disable_account.error')}{username}
                </Alert>
            </Snackbar>

            <Snackbar open={blockedUserOpen} autoHideDuration={6000} onClose={handleBlockedUserOpen}>
                <Alert onClose={handleBlockedUserOpen} severity="info" sx={{width: '100%'}}>
                    {t('disable_account.blocked_user_one')+username+t('disable_account.blocked_user_two')}
                </Alert>
            </Snackbar>

            <Snackbar open={authorizationErrorOpen} autoHideDuration={6000} onClose={handleAuthorizationErrorOpen}>
                <Alert onClose={handleAuthorizationErrorOpen} severity="error" sx={{width: '100%'}}>
                    {t('disable_account.authorization_error')}
                </Alert>
            </Snackbar>
        </div>
    );
}

export default DisableAccount;