import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { Alternativa } from 'app/entities/enumerations/alternativa.model';
import { IResposta, Resposta } from '../resposta.model';

import { RespostaService } from './resposta.service';

describe('Resposta Service', () => {
  let service: RespostaService;
  let httpMock: HttpTestingController;
  let elemDefault: IResposta;
  let expectedResult: IResposta | IResposta[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RespostaService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      respostaData: currentDate,
      respostaAlternativa: Alternativa.A,
      respostaCerta: false,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          respostaData: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Resposta', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          respostaData: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          respostaData: currentDate,
        },
        returnedFromService
      );

      service.create(new Resposta()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Resposta', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          respostaData: currentDate.format(DATE_TIME_FORMAT),
          respostaAlternativa: 'BBBBBB',
          respostaCerta: true,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          respostaData: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Resposta', () => {
      const patchObject = Object.assign(
        {
          respostaAlternativa: 'BBBBBB',
          respostaCerta: true,
        },
        new Resposta()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          respostaData: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Resposta', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          respostaData: currentDate.format(DATE_TIME_FORMAT),
          respostaAlternativa: 'BBBBBB',
          respostaCerta: true,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          respostaData: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Resposta', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addRespostaToCollectionIfMissing', () => {
      it('should add a Resposta to an empty array', () => {
        const resposta: IResposta = { id: 123 };
        expectedResult = service.addRespostaToCollectionIfMissing([], resposta);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(resposta);
      });

      it('should not add a Resposta to an array that contains it', () => {
        const resposta: IResposta = { id: 123 };
        const respostaCollection: IResposta[] = [
          {
            ...resposta,
          },
          { id: 456 },
        ];
        expectedResult = service.addRespostaToCollectionIfMissing(respostaCollection, resposta);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Resposta to an array that doesn't contain it", () => {
        const resposta: IResposta = { id: 123 };
        const respostaCollection: IResposta[] = [{ id: 456 }];
        expectedResult = service.addRespostaToCollectionIfMissing(respostaCollection, resposta);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(resposta);
      });

      it('should add only unique Resposta to an array', () => {
        const respostaArray: IResposta[] = [{ id: 123 }, { id: 456 }, { id: 72712 }];
        const respostaCollection: IResposta[] = [{ id: 123 }];
        expectedResult = service.addRespostaToCollectionIfMissing(respostaCollection, ...respostaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const resposta: IResposta = { id: 123 };
        const resposta2: IResposta = { id: 456 };
        expectedResult = service.addRespostaToCollectionIfMissing([], resposta, resposta2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(resposta);
        expect(expectedResult).toContain(resposta2);
      });

      it('should accept null and undefined values', () => {
        const resposta: IResposta = { id: 123 };
        expectedResult = service.addRespostaToCollectionIfMissing([], null, resposta, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(resposta);
      });

      it('should return initial array if no Resposta is added', () => {
        const respostaCollection: IResposta[] = [{ id: 123 }];
        expectedResult = service.addRespostaToCollectionIfMissing(respostaCollection, undefined, null);
        expect(expectedResult).toEqual(respostaCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
