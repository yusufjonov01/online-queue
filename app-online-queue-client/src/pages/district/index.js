import React, {Component} from 'react';
import {Button, Col, Container, CustomInput, Modal, ModalBody, ModalFooter, ModalHeader, Row, Table} from "reactstrap";
import {connect} from "dva";
import {AvField, AvForm} from 'availity-reactstrap-validation';

@connect(({districtModel}) => ({districtModel}))
class District extends Component {
  componentDidMount() {
    const {dispatch} = this.props;
    dispatch({
      type: 'districtModel/getDistricts'
    });
    dispatch({
      type: 'districtModel/getRegions'
    })
  }

  render() {
    const {dispatch, districtModel} = this.props;
    const {showModal, regions, districts, showDeleteModal, currentDistrict} = districtModel;


    //MODALNI OCHISH FUNKSIYASI
    const openModal = (item) => {
      dispatch({
        type: 'districtModel/updateState',
        payload: {
          showModal: !showModal,
          currentDistrict: item
        }
      })
    };

    const openDeleteModal = (item) => {
      dispatch({
        type: 'districtModel/updateState',
        payload: {
          showDeleteModal: !showDeleteModal,
          currentDistrict: item
        }
      })
    };
    const deleteDistrict = () => {
      dispatch({
        type: 'districtModel/deleteDistrict',
        payload: {
          id: currentDistrict.id,
        }
      })
    };

    //District NI SAQLASH VA EDIT QILISH FUNKSIYASI
    const saveDistrict = (e, v) => {
      dispatch({
        type: 'districtModel/addDistrict',
        payload: {
          id: currentDistrict.id,
          ...v,
        }
      })
    };

    return (
      <div>
        <Container className="mt-4">
          <Row>
            <Col>
              <h4 className="text-center">District list</h4>
            </Col>
          </Row>
          <Row>
            <Button color="primary" onClick={() => openModal('')}>
              Add District
            </Button>
          </Row>
          <Row className="mt-3">
            <Col>
              <Table>
                <thead>
                <tr>
                  <th>Tr</th>
                  <th>Name</th>
                  <th colSpan="2">Action</th>
                </tr>
                </thead>
                <tbody>
                {districts.map((item, i) =>
                  <tr key={item.id}>
                    <td>{i + 1}</td>
                    <td>{item.name}</td>
                    <td>{item.region.name}</td>
                    <td><Button color="warning" onClick={() => openModal(item)}>Edit</Button></td>
                    <td><Button color="danger" onClick={() => openDeleteModal(item)}>Delete</Button></td>
                  </tr>
                )}
                </tbody>
              </Table>
            </Col>
          </Row>
        </Container>

        <Modal isOpen={showModal}>
          <AvForm onValidSubmit={saveDistrict}>
            <ModalHeader>
              {currentDistrict.name ? 'Edit district' : 'Add district'}
            </ModalHeader>
            <ModalBody>
              <AvField defaultValue={currentDistrict.name} name="name"
                       label="District name" required placeholder="Enter district name"/>
              <AvField name="region" type="select" value={currentDistrict.name ? "regionId" : "0"} required>
                <option value="0" disabled={true}>Viloyatni tanlang</option>
                {regions.map(item =>
                  <option key={item.id} value={"/" + item.id}>{item.name}</option>
                )}
              </AvField>
            </ModalBody>
            <ModalFooter>
              <Button color="danger" type="button" onClick={openModal}>Cancel</Button>
              <Button color="success">Save</Button>
            </ModalFooter>
          </AvForm>
        </Modal>

        <Modal isOpen={showDeleteModal}>
          <ModalHeader>
            {'Are you sure delete ' + currentDistrict.name + '?'}
          </ModalHeader>
          <ModalFooter>
            <Button color="primary" onClick={openDeleteModal}>No</Button>
            <Button color="danger" onClick={deleteDistrict}>Yes</Button>
          </ModalFooter>
        </Modal>
      </div>
    );
  }
}

District.propTypes = {};

export default District;
