import axios from "axios";
import {API_PREFIX} from "@/constants/constants";
import request from "./utils";

export function saveCategory(data) {
  return request({
    url: 'category',
    method: data.id ? 'PUT' : 'POST',
    data
  })
}

export function getCategories() {
  return request({
    url: 'category'
  })
}

export function deleteCategory(data) {
  return request({
    url: 'category' + '/' + data.id,
    method: 'Delete'
  })
}

export function saveRegion(data) {
  console.log(data);
  return request({
    url: 'region',
    method: data.id ? 'PUT' : 'POST',
    data
  })
}

export function deleteRegion(data) {
  return request({
    url: 'region' + '/' + data.id,
    method: 'Delete'
  })
}

export function getRegions() {
  return request({
    url: 'region'
  })
}

export function saveDistrict(data) {
  return request({
    url: 'district',
    method: data.id ? 'PUT' : 'POST',
    data
  })
}

export function getDistricts() {
  return request({
    url: 'district'
  })
}

export function deleteDistrict(data) {
  return request({
    url: 'district' + '/' + data.id,
    method: 'Delete'
  })
}

export function saveAware(data) {
  return request({
    url: 'aware',
    method: data.id ? 'PUT' : 'POST',
    data
  })
}

export function getAwares() {
  return request({
    url: 'aware'
  })
}

export function deleteAware(data) {
  return request({
    url: 'aware' + '/' + data.id,
    method: 'Delete'
  })
}

export function userMe() {
  return request({
    url: 'user/userMe',
  })
}

export function login(data) {
  return request({
    url: 'auth/login',
    method: 'post',
    data
  })
}

export function getCompanies(data) {
  return request({
    url: `company?page=${data.page}&size=${data.size}`
  });
}

export function getCompany(data) {
  return request(
    {url: 'company' + data.id})

}

export function getMessages() {
  return request(
    {
      url: 'message'
    }
  )
}

export function viewChange(data) {
  return request(
    {
      url: 'message/' + data.id,
      method: 'put'
    }
  )
}


//EDITPASSWORD
export function editPassword(data) {
  return request({
    url: 'user/editPassword',
    method: 'Put',
    data
  })
}






















