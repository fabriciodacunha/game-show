import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IParticipante, Participante } from '../participante.model';

import { ParticipanteService } from './participante.service';

describe('Participante Service', () => {
  let service: ParticipanteService;
  let httpMock: HttpTestingController;
  let elemDefault: IParticipante;
  let expectedResult: IParticipante | IParticipante[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ParticipanteService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      carastroUsuario: 'AAAAAAA',
      participanteNome: 'AAAAAAA',
      participanteEmail: 'AAAAAAA',
      participanteDataDeNascimento: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          participanteDataDeNascimento: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Participante', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          participanteDataDeNascimento: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          participanteDataDeNascimento: currentDate,
        },
        returnedFromService
      );

      service.create(new Participante()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Participante', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          carastroUsuario: 'BBBBBB',
          participanteNome: 'BBBBBB',
          participanteEmail: 'BBBBBB',
          participanteDataDeNascimento: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          participanteDataDeNascimento: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Participante', () => {
      const patchObject = Object.assign(
        {
          carastroUsuario: 'BBBBBB',
          participanteDataDeNascimento: currentDate.format(DATE_FORMAT),
        },
        new Participante()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          participanteDataDeNascimento: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Participante', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          carastroUsuario: 'BBBBBB',
          participanteNome: 'BBBBBB',
          participanteEmail: 'BBBBBB',
          participanteDataDeNascimento: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          participanteDataDeNascimento: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Participante', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addParticipanteToCollectionIfMissing', () => {
      it('should add a Participante to an empty array', () => {
        const participante: IParticipante = { id: 123 };
        expectedResult = service.addParticipanteToCollectionIfMissing([], participante);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(participante);
      });

      it('should not add a Participante to an array that contains it', () => {
        const participante: IParticipante = { id: 123 };
        const participanteCollection: IParticipante[] = [
          {
            ...participante,
          },
          { id: 456 },
        ];
        expectedResult = service.addParticipanteToCollectionIfMissing(participanteCollection, participante);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Participante to an array that doesn't contain it", () => {
        const participante: IParticipante = { id: 123 };
        const participanteCollection: IParticipante[] = [{ id: 456 }];
        expectedResult = service.addParticipanteToCollectionIfMissing(participanteCollection, participante);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(participante);
      });

      it('should add only unique Participante to an array', () => {
        const participanteArray: IParticipante[] = [{ id: 123 }, { id: 456 }, { id: 15066 }];
        const participanteCollection: IParticipante[] = [{ id: 123 }];
        expectedResult = service.addParticipanteToCollectionIfMissing(participanteCollection, ...participanteArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const participante: IParticipante = { id: 123 };
        const participante2: IParticipante = { id: 456 };
        expectedResult = service.addParticipanteToCollectionIfMissing([], participante, participante2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(participante);
        expect(expectedResult).toContain(participante2);
      });

      it('should accept null and undefined values', () => {
        const participante: IParticipante = { id: 123 };
        expectedResult = service.addParticipanteToCollectionIfMissing([], null, participante, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(participante);
      });

      it('should return initial array if no Participante is added', () => {
        const participanteCollection: IParticipante[] = [{ id: 123 }];
        expectedResult = service.addParticipanteToCollectionIfMissing(participanteCollection, undefined, null);
        expect(expectedResult).toEqual(participanteCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
