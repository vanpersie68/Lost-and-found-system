import { getRequest, postRequest, putRequest, postBodyRequest, getNoAuthRequest, postNoAuthRequest } from '@/libs/axios';

export const getDrawOne = (params) => {
    return getRequest('/draw/getOne', params)
}
export const getDrawList = (params) => {
    return getRequest('/draw/getByPage', params)
}
export const getDrawList2 = (params) => {
    return getRequest('/draw/getByPage2', params)
}
export const getDrawCount = (params) => {
    return getRequest('/draw/count', params)
}
export const addDraw = (params) => {
    return postRequest('/draw/insert', params)
}
export const editDraw = (params) => {
    return postRequest('/draw/update', params)
}
export const addOrEditDraw = (params) => {
    return postRequest('/draw/insertOrUpdate', params)
}
export const deleteDraw = (params) => {
    return postRequest('/draw/delByIds', params)
}