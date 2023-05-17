import * as React from 'react';
import Button from '@mui/material/Button';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogTitle from '@mui/material/DialogTitle';
import axios from 'axios'; 
import { API_URL } from '../../consts';
import { useParams} from "react-router-dom";
import {useCookies} from 'react-cookie';
import {useTranslation} from "react-i18next";
import { useEffect } from 'react';

const EnableAccount = () => {
  const {t, i18n} = useTranslation();
  const username = useParams().username;
  const [cookies, setCookie] = useCookies(["token", "etag"]);
  const token = "Bearer " + cookies.token;
  const etag = cookies.etag;

  const [open, setOpen] = React.useState(false);
  const [confirmOpen, setConfirmOpen] = React.useState(false);

  const [successOpen, setSuccessOpen] = React.useState(false);
  const [errorOpen, setErrorOpen] = React.useState(false);

    const fetchData = async () => {
      await axios.get(`${API_URL}/accounts/${username}`, {
        headers: {
          Authorization: token
        }
      })
      .then(response => {
        setCookie("etag", response.headers.etag);
      });
  };

  const enable = async () => {
    axios.patch(`${API_URL}/accounts/${username}/enable`, {},
    {
       headers: {
        'Authorization': token,
        'If-Match': etag
      },
    })
    .then(response => {
      setSuccessOpen(true);
    })
    .catch(error => {
      setErrorOpen(true);
    });
  };

  const handleClickOpen = () => {
    fetchData();
    setOpen(true);
  };

  const handleConfirmClose = (event: React.SyntheticEvent<unknown>, reason?: string) => {
    if (reason !== 'backdropClick') {
      setOpen(false);
    }
  }

  const handleConfirmConfirm = (event: React.SyntheticEvent<unknown>, reason?: string) => {
    if (reason !== 'backdropClick') {
      setConfirmOpen(false);
    }
    enable();
    handleConfirmClose(event, reason);
  }

  const handleSuccessClose = (event: React.SyntheticEvent<unknown>, reason?: string) => {
    if (reason !== 'backdropClick') {
      setSuccessOpen(false);
    }
  }

  const handleErrorClose = (event: React.SyntheticEvent<unknown>, reason?: string) => {
    if (reason !== 'backdropClick') {
      setErrorOpen(false);
    }
  };

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

      <Dialog disableEscapeKeyDown open={successOpen}>
        <DialogTitle>{t('enable_account.success_one')}{username}{t('enable_account.success_two')}</DialogTitle>
        <Button onClick={handleSuccessClose}>{t('confirm.ok')}</Button>
      </Dialog>

      <Dialog disableEscapeKeyDown open={errorOpen}>
        <DialogTitle>{t('enable_account.error')}{username}</DialogTitle>
        <Button onClick={handleErrorClose}>{t('confirm.ok')}</Button>
      </Dialog>
    </div>
  );
}
export default EnableAccount;