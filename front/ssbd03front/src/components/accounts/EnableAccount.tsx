import * as React from 'react';
import {useState} from 'react';
import Button from '@mui/material/Button';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogTitle from '@mui/material/DialogTitle';
import { Snackbar, SnackbarContent } from '@mui/material';
import axios from 'axios';
import {API_URL} from '../../consts';
import {useParams} from "react-router-dom";
import {useTranslation} from "react-i18next";

const EnableAccount = () => {
    const {t, i18n} = useTranslation();
    const username = useParams().username;
    const token = "Bearer " + localStorage.getItem("token");
    const [version, setVersion] = useState("");
    const [enableState, setEnable] = useState(false);

    const [open, setOpen] = useState(false);
    const [confirmOpen, setConfirmOpen] = useState(false);

    const [successOpen, setSuccessOpen] = useState(false);
    const [errorOpen, setErrorOpen] = useState(false);

    const [unblockedUserOpen, setUnblockedUserOpen] = useState(false);

    const [authorizationErrorOpen, setAuthorizationErrorOpen] = useState(false);

    const fetchData = async () => {
        await axios.get(`${API_URL}/accounts/${username}`, {
            headers: {
                Authorization: token
            }
        })
            .then(response => {
                setVersion(response.data.version);
                setEnable(response.data.isEnable);
            })
            .catch(error => {
                if (error.response.status === 403) {
                    setAuthorizationErrorOpen(true);
                    return;
                }
            });
    };

    const enable = async () => {
        axios.patch(`${API_URL}/accounts/${username}/enable`,
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

    const handleConfirmConfirm = () => {
        setConfirmOpen(false);
        if (enableState) {
            setUnblockedUserOpen(true);
            return;
        }
        enable();
        handleConfirmClose();
    }

    const handleSuccessClose = () => {
        setSuccessOpen(false);
        window.location.reload();
    }

    const handleErrorClose = () => {
        setErrorOpen(false);
    };

    const handleUnblockedUserOpen = () => {
        setUnblockedUserOpen(false);
        handleConfirmClose();
    };

    const handleAuthorizationErrorOpen = () => {
        setAuthorizationErrorOpen(false);
        handleConfirmClose();
    };

    setTimeout(handleSuccessClose, 20000);
    setTimeout(handleErrorClose, 20000);
    setTimeout(handleUnblockedUserOpen, 20000);
    setTimeout(handleAuthorizationErrorOpen, 20000);

    return (
        <div>
            <div>
                <Button onClick={handleClickOpen} variant="contained">{t('enable_account.enable')}</Button>
            </div>
            <Dialog disableEscapeKeyDown open={open}>
                <DialogTitle>{t('enable_account.enable_confirm')}{username}?</DialogTitle>
                <DialogActions>
                    <Button onClick={handleConfirmClose}>{t('confirm.no')}</Button>
                    <Button onClick={handleConfirmConfirm}>{t('confirm.yes')}</Button>
                </DialogActions>
            </Dialog>

            <Snackbar open={successOpen} onClose={handleSuccessClose}>
                <SnackbarContent 
                message={t('enable_account.success_one')+username+t('enable_account.success_two')}/>
            </Snackbar>

            <Snackbar open={errorOpen} onClose={handleErrorClose}>
                <SnackbarContent 
                message={t('enable_account.error')+username}/>
            </Snackbar>

            <Snackbar open={unblockedUserOpen} onClose={handleUnblockedUserOpen}>
                <SnackbarContent 
                message={t('enable_account.unblocked_user_one')+username+t('enable_account.unblocked_user_two')}/>
            </Snackbar>

            <Snackbar open={authorizationErrorOpen} onClose={handleAuthorizationErrorOpen}>
                <SnackbarContent 
                message={t('enable_account.authorization_error')}/>
            </Snackbar>
        </div>
    );
}
export default EnableAccount;