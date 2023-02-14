/* eslint no-param-reassign: ["error", { "props": false }] */
import _ from 'lodash';
import { addTranslationForLanguage } from 'react-localize-redux';

import {
  CHANGE_CURRENT_LOCALE,
  CHANGE_CURRENT_LOCATION,
  FETCH_BUYERS,
  FETCH_CONFIG,
  FETCH_CONFIG_AND_SET_ACTIVE,
  FETCH_CURRENCIES,
  FETCH_GRAPHS,
  FETCH_INVOICE_STATUSES,
  FETCH_INVOICE_TYPE_CODES,
  FETCH_MENU_CONFIG,
  FETCH_NUMBERS,
  FETCH_PURCHASE_ORDER_STATUSES,
  FETCH_REASONCODES,
  FETCH_REQUISITION_STATUS_CODES,
  FETCH_SESSION_INFO,
  FETCH_SHIPMENT_STATUS_CODES,
  FETCH_STOCK_TRANSFER_STATUSES,
  FETCH_SUPPLIERS,
  FETCH_USERS,
  FILTER_FORM_PARAMS_BUILT,
  HIDE_SPINNER,
  REBUILD_FILTER_FORM_PARAMS,
  REMOVE_FROM_INDICATORS,
  REORDER_INDICATORS,
  RESET_INDICATORS,
  SET_ACTIVE_CONFIG,
  SHOW_SPINNER,
  TOGGLE_USER_ACTION_MENU,
  TRANSLATIONS_FETCHED,
} from 'actions/types';
import apiClient, { parseResponse } from 'utils/apiClient';

export function showSpinner() {
  return {
    type: SHOW_SPINNER,
    payload: true,
  };
}

export function hideSpinner() {
  return {
    type: HIDE_SPINNER,
    payload: false,
  };
}

export function showUserActions() {
  return {
    type: TOGGLE_USER_ACTION_MENU,
    payload: true,
  };
}

export function hideUserActions() {
  return {
    type: TOGGLE_USER_ACTION_MENU,
    payload: false,
  };
}

export function fetchReasonCodes() {
  const url = '/api/reasonCodes';
  return (dispatch) => {
    apiClient.get(url).then((res) => {
      dispatch({
        type: FETCH_REASONCODES,
        payload: res.data,
      });
    });
  };
}

export function fetchCurrencies() {
  const url = '/api/unitOfMeasure/currencies';
  return (dispatch) => {
    apiClient.get(url).then((res) => {
      dispatch({
        type: FETCH_CURRENCIES,
        payload: res.data,
      });
    });
  };
}

export function fetchUsers() {
  const url = '/api/persons';
  return (dispatch) => {
    apiClient.get(url, { params: { status: true } }).then((res) => {
      dispatch({
        type: FETCH_USERS,
        payload: res.data,
      });
    });
  };
}

export async function fetchSessionInfo() {
  const url = '/api/getAppContext';
  const res = await apiClient.get(url);

  return (dispatch) => {
    dispatch({
      type: FETCH_SESSION_INFO,
      payload: res,
    });
  };
}

export function fetchMenuConfig() {
  const url = '/api/getMenuConfig';
  return (dispatch) => {
    apiClient.get(url).then((res) => {
      dispatch({
        type: FETCH_MENU_CONFIG,
        payload: res,
      });
    });
  };
}

export function changeCurrentLocation(location) {
  return (dispatch) => {
    const url = `/api/chooseLocation/${location.id}`;

    return apiClient.put(url).then(() => {
      dispatch({
        type: CHANGE_CURRENT_LOCATION,
        payload: location,
      });
    });
  };
}

export function fetchTranslations(lang, prefix) {
  return (dispatch) => {
    const url = `/api/localizations?lang=${lang ||
      ''}&prefix=react.${prefix || ''}`;

    apiClient.get(url).then((response) => {
      const { messages, currentLocale } = parseResponse(response.data);

      dispatch(addTranslationForLanguage(messages, currentLocale));

      dispatch({
        type: TRANSLATIONS_FETCHED,
        payload: prefix,
      });
    });
  };
}

export function changeCurrentLocale(locale) {
  return (dispatch) => {
    const url = `/api/chooseLocale/${locale}`;

    apiClient.put(url).then(() => {
      dispatch({
        type: CHANGE_CURRENT_LOCALE,
        payload: locale,
      });
    });
  };
}

// New Dashboard

function getParameterList(params = '', locationId = '', userId = '') {
  const listFiltersSelected = [];
  const listValues = [];

  const dashboardKey = sessionStorage.getItem('dashboardKey');

  const pageConfig = JSON.parse(sessionStorage.getItem('pageConfig')) || {};

  if (!pageConfig[dashboardKey]) { pageConfig[dashboardKey] = {}; }

  let listParams = params === '' ? `locationId=${locationId}` : `${params}&locationId=${locationId}`;
  listParams += userId ? `&userId=${userId}` : '';

  // List of filter and category
  // filter[0] is the category
  // filter[1] represent the values
  Object.entries(pageConfig[dashboardKey]).forEach((filter) => {
    listFiltersSelected.push(filter[0]);
    filter[1].forEach(value => listValues.push(value));
  });
  // Add condition to check if currentPage has any filter
  listFiltersSelected.forEach((filter) => {
    listParams = `${listParams}&listFiltersSelected=${filter}`;
  });
  listValues.forEach((value) => {
    listParams = `${listParams}&value=${value.id}`;
  });

  return listParams;
}

function fetchGraphIndicator(
  dispatch,
  indicatorConfig,
  locationId = '',
  params = '',
) {
  const id = indicatorConfig.order;

  const listParams = getParameterList(params, locationId);
  const url = `${indicatorConfig.endpoint}?${listParams}`;

  dispatch({
    type: FETCH_GRAPHS,
    payload: {
      id,
      widgetId: indicatorConfig.widgetId,
      title: 'Loading...',
      info: 'Loading...',
      type: 'loading',
      data: [],
    },
  });

  apiClient.get(url).then((res) => {
    const indicatorData = res.data;
    dispatch({
      type: FETCH_GRAPHS,
      payload: {
        id,
        widgetId: indicatorConfig.widgetId,
        title: indicatorConfig.title,
        info: indicatorConfig.info,
        type: indicatorConfig.graphType,
        data: indicatorData.data,
        timeFilter: indicatorConfig.timeFilter,
        yearTypeFilter: indicatorConfig.yearTypeFilter,
        locationFilter: indicatorConfig.locationFilter,
        timeLimit: indicatorConfig.timeLimit,
        link: indicatorData.link,
        legend: indicatorConfig.legend,
        doubleAxeY: indicatorConfig.doubleAxeY,
        config: {
          stacked: indicatorConfig.stacked,
          datalabel: indicatorConfig.datalabel,
          colors: indicatorConfig.colors,
        },
        size: indicatorConfig.size,
      },
    });
  }, () => {
    dispatch({
      type: FETCH_GRAPHS,
      payload: {
        id,
        widgetId: indicatorConfig.widgetId,
        title: 'Indicator could not be loaded',
        type: 'error',
        data: [],
      },
    });
  });
}

function fetchNumberIndicator(
  dispatch,
  indicatorConfig,
  locationId,
  userId,
) {
  const id = indicatorConfig.order;

  const listParams = getParameterList('', locationId, userId);

  const url = `${indicatorConfig.endpoint}?${listParams}`;

  apiClient.get(url).then((res) => {
    const indicatorData = res.data;
    dispatch({
      type: FETCH_NUMBERS,
      payload: {
        ...indicatorData,
        id,
        widgetId: indicatorConfig.widgetId,
        title: indicatorConfig.title,
        info: indicatorConfig.info,
        subtitle: indicatorConfig.subtitle,
        numberType: indicatorConfig.numberType,
      },
    });
  });
}

export function reloadIndicator(indicatorConfig, params, locationId) {
  return (dispatch) => {
    // new reference so that the original config is not modified
    const indicatorConfigData = JSON.parse(JSON.stringify(indicatorConfig));
    fetchGraphIndicator(dispatch, indicatorConfigData, locationId, params);
  };
}

function getData(dispatch, dashboardConfig, locationId, config = 'personal', userId = '') {
  // new reference so that the original config is not modified

  const dashboard = dashboardConfig.dashboard[config] || {};
  const widgets = _.map(dashboard.widgets, widget => ({
    ...dashboardConfig.dashboardWidgets[widget.widgetId],
    order: widget.order,
    widgetId: widget.widgetId,
  }));

  const visibleWidgets = _.chain(widgets)
    .filter(widget => widget.enabled)
    .sortBy(['order']).value();

  _.forEach(visibleWidgets, (widgetConfig) => {
    if (widgetConfig.type === 'graph') {
      fetchGraphIndicator(dispatch, widgetConfig, locationId, '');
    } else if (widgetConfig.type === 'number') {
      fetchNumberIndicator(dispatch, widgetConfig, locationId, userId);
    }
  });
}

export function fetchIndicators(
  configData,
  config,
  locationId,
  userId,
) {
  return (dispatch) => {
    dispatch({
      type: SET_ACTIVE_CONFIG,
      payload: {
        data: config,
      },
    });

    getData(dispatch, configData, locationId, config, userId);
  };
}

export function resetIndicators() {
  return {
    type: RESET_INDICATORS,
  };
}

export function addToIndicators(widgetConfig, locationId, userId = '') {
  return (dispatch) => {
    if (widgetConfig.type === 'graph') {
      fetchGraphIndicator(dispatch, widgetConfig, locationId, '');
    } else if (widgetConfig.type === 'number') {
      fetchNumberIndicator(dispatch, widgetConfig, locationId, userId);
    }
  };
}

export function reorderIndicators({ oldIndex, newIndex }, e, type) {
  if (e.target.id === 'archive') {
    return {
      type: REMOVE_FROM_INDICATORS,
      payload: { index: oldIndex, type },
    };
  }
  return {
    type: REORDER_INDICATORS,
    payload: { oldIndex, newIndex, type },
  };
}

// eslint-disable-next-line default-param-last
export function fetchConfigAndData(locationId, config = 'personal', userId, id, filterSelected) {
  return (dispatch) => {
    apiClient.get(`/api/dashboard/${id}/config`).then((res) => {
      dispatch({
        type: FETCH_CONFIG_AND_SET_ACTIVE,
        payload: {
          data: res.data,
          activeConfig: config,
        },
      });
      getData(dispatch, res.data, locationId, config, userId, filterSelected);
    });
  };
}

export function fetchConfig(id) {
  return (dispatch) => {
    apiClient.get(`/api/dashboard/${id}/config`).then((res) => {
      dispatch({
        type: FETCH_CONFIG,
        payload: {
          data: res.data,
        },
      });
    });
  };
}

export function fetchPurchaseOrderStatuses() {
  return (dispatch) => {
    apiClient.get('/api/orderSummaryStatus').then((res) => {
      dispatch({
        type: FETCH_PURCHASE_ORDER_STATUSES,
        payload: res.data.data,
      });
    });
  };
}

export function fetchSuppliers(active = false) {
  return (dispatch) => {
    apiClient.get(`/api/organizations?roleType=ROLE_SUPPLIER&active=${active}`)
      .then((res) => {
        if (res.data.data) {
          const suppliers = res.data.data.map(obj => (
            {
              id: obj.id,
              value: obj.id,
              name: obj.name,
              label: `${obj.name}`,
            }
          ));
          dispatch({
            type: FETCH_SUPPLIERS,
            payload: suppliers,
          });
          return;
        }
        dispatch({
          type: FETCH_SUPPLIERS,
          payload: [],
        });
      });
  };
}

export function fetchBuyers(active = false) {
  return (dispatch) => {
    apiClient.get(`/api/organizations?roleType=ROLE_BUYER&active=${active}`)
      .then((res) => {
        if (res.data.data) {
          const buyers = res.data.data.map(obj => (
            {
              id: obj.id,
              value: obj.id,
              name: obj.name,
              label: `${obj.name}`,
            }
          ));
          dispatch({
            type: FETCH_BUYERS,
            payload: buyers,
          });
          return;
        }
        dispatch({
          type: FETCH_BUYERS,
          payload: [],
        });
      });
  };
}

export function fetchInvoiceStatuses() {
  return (dispatch) => {
    apiClient.get('/api/invoiceStatuses').then((res) => {
      dispatch({
        type: FETCH_INVOICE_STATUSES,
        payload: res.data.data,
      });
    });
  };
}

export function fetchInvoiceTypeCodes() {
  return (dispatch) => {
    apiClient.get('/api/invoiceTypeCodes').then((res) => {
      dispatch({
        type: FETCH_INVOICE_TYPE_CODES,
        payload: res.data.data,
      });
    });
  };
}

export function fetchShipmentStatusCodes() {
  return (dispatch) => {
    apiClient.get('/api/stockMovements/shipmentStatusCodes')
      .then((res) => {
        dispatch({
          type: FETCH_SHIPMENT_STATUS_CODES,
          payload: res.data.data,
        });
      });
  };
}

export function fetchRequisitionStatusCodes() {
  return (dispatch) => {
    apiClient.get('/api/stockMovements/requisitionsStatusCodes')
      .then((res) => {
        dispatch({
          type: FETCH_REQUISITION_STATUS_CODES,
          payload: res.data.data,
        });
      });
  };
}

export function fetchStockTransferStatuses() {
  return (dispatch) => {
    apiClient.get('/api/stockTransfers/statusOptions').then((res) => {
      dispatch({
        type: FETCH_STOCK_TRANSFER_STATUSES,
        payload: res.data.data,
      });
    });
  };
}

export const setShouldRebuildFilterParams = (flag = true) => (dispatch) => {
  // if flag is true, we want to trigger the rebuild of filter form params
  if (flag) {
    return dispatch({
      type: REBUILD_FILTER_FORM_PARAMS,
    });
  }
  // otherwise we want to unmark it to the "standby" (false) position
  return dispatch({
    type: FILTER_FORM_PARAMS_BUILT,
  });
};
