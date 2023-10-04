import { getRequest, postRequest, putRequest, postBodyRequest, getNoAuthRequest, postNoAuthRequest } from '@/libs/axios';

export const getLookThingsOne = (params) => {
    return getRequest('/lookThings/getOne', params)
}
export const getLookThingsList = (params) => {
    return getRequest('/lookThings/getByPage', params)
}
export const getLookThingsList2 = (params) => {
    return getRequest('/lookThings/getByPage2', params)
}
export const getLookThingsCount = (params) => {
    return getRequest('/lookThings/count', params)
}
export const addLookThings = (params) => {
    return postRequest('/lookThings/insert', params)
}
export const editLookThings = (params) => {
    return postRequest('/lookThings/update', params)
}
export const addOrEditLookThings = (params) => {
    return postRequest('/lookThings/insertOrUpdate', params)
}
export const deleteLookThings = (params) => {
    return postRequest('/lookThings/delByIds', params)
}