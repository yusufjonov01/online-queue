import React, {Component} from 'react';
import {Button, Col, Container, CustomInput, Modal, ModalBody, ModalFooter, ModalHeader, Row, Table} from "reactstrap";
import {connect} from "dva";
import {AvField, AvForm} from 'availity-reactstrap-validation';
import {login} from "../service";

@connect(({regionModel}) => ({regionModel}))
class Region extends Component {
  componentDidMount() {
    const {dispatch} = this.props;
    dispatch({
      type: 'regionModel/getRegions'
    })
  }

  render() {
    const {dispatch, regionModel} = this.props;
    const {showModal, regions, showDeleteModal, currentItem} = regionModel;


    //MODALNI OCHISH FUNKSIYASI
    const openModal = (item) => {
      dispatch({
        type: 'regionModel/updateState',
        payload: {
          showModal: !showModal,
          currentItem: item
        }
      })
    };

    const openDeleteModal = (item) => {
      dispatch({
        type: 'regionModel/updateState',
        payload: {
          showDeleteModal: !showDeleteModal,
          currentItem: item
        }
      })
    };
    const deleteRegion = () => {
      dispatch({
        type: 'regionModel/deleteRegion',
        payload: {
          id: currentItem.id,
        }
      })
    };

    const saveRegion = (e, v) => {
      console.log(v);
      dispatch({
        type: "regionModel/addReg",
        payload: {
          id: currentItem.id,
          ...v
        }
      });
      console.log(145);
    };


    return (
      <div>
        <Container className="mt-4">
          <Row>
            <Col>
              <h4 className="text-center">Region list</h4>
            </Col>
          </Row>
          <Row>
            <Button color="primary" onClick={() => openModal('')}>
              Add Region
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
                {regions.map((item, i) =>
                  <tr key={item.id}>
                    <td>{i + 1}</td>
                    <td>{item.nameUzk}</td>
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
          <AvForm onValidSubmit={saveRegion}>
            <ModalHeader>
              {currentItem.nameUzk ? 'Edit region' : 'Add region'}
            </ModalHeader>
            <ModalBody>
              <AvField defaultValue={currentItem.nameUzk} name="nameUzk"
                       label="Region name" required placeholder="Enter region name"/>
              <AvField defaultValue={currentItem.nameUzl} name="nameUzl"
                       label="Region name" required placeholder="Enter region name"/>
              <AvField defaultValue={currentItem.nameRuk} name="nameRu"
                       label="Region name" required placeholder="Enter region name"/>
              <AvField defaultValue={currentItem.nameEnk} name="nameEn"
                       label="Region name" required placeholder="Enter region name"/>

            </ModalBody>
            <ModalFooter>
              <Button color="danger" type="button" onClick={openModal}>Cancel</Button>
              <Button color="success">Save</Button>
            </ModalFooter>
          </AvForm>
        </Modal>

        <Modal isOpen={showDeleteModal}>
          <ModalHeader>
            {'Are you sure delete ' + currentItem.nameUzk + '?'}
          </ModalHeader>
          <ModalFooter>
            <Button color="primary" onClick={openDeleteModal}>No</Button>
            <Button color="danger" onClick={deleteRegion}>Yes</Button>
          </ModalFooter>
        </Modal>
      </div>
    );
  }
}

Region.propTypes = {};

export default Region;
