import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IEdicao, Edicao } from '../edicao.model';

import { EdicaoService } from './edicao.service';

describe('Edicao Service', () => {
  let service: EdicaoService;
  let httpMock: HttpTestingController;
  let elemDefault: IEdicao;
  let expectedResult: IEdicao | IEdicao[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(EdicaoService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      edicaoTitulo: 'AAAAAAA',
      edicaoData: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          edicaoData: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Edicao', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          edicaoData: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          edicaoData: currentDate,
        },
        returnedFromService
      );

      service.create(new Edicao()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Edicao', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          edicaoTitulo: 'BBBBBB',
          edicaoData: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          edicaoData: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Edicao', () => {
      const patchObject = Object.assign({}, new Edicao());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          edicaoData: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Edicao', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          edicaoTitulo: 'BBBBBB',
          edicaoData: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          edicaoData: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Edicao', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addEdicaoToCollectionIfMissing', () => {
      it('should add a Edicao to an empty array', () => {
        const edicao: IEdicao = { id: 123 };
        expectedResult = service.addEdicaoToCollectionIfMissing([], edicao);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(edicao);
      });

      it('should not add a Edicao to an array that contains it', () => {
        const edicao: IEdicao = { id: 123 };
        const edicaoCollection: IEdicao[] = [
          {
            ...edicao,
          },
          { id: 456 },
        ];
        expectedResult = service.addEdicaoToCollectionIfMissing(edicaoCollection, edicao);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Edicao to an array that doesn't contain it", () => {
        const edicao: IEdicao = { id: 123 };
        const edicaoCollection: IEdicao[] = [{ id: 456 }];
        expectedResult = service.addEdicaoToCollectionIfMissing(edicaoCollection, edicao);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(edicao);
      });

      it('should add only unique Edicao to an array', () => {
        const edicaoArray: IEdicao[] = [{ id: 123 }, { id: 456 }, { id: 35956 }];
        const edicaoCollection: IEdicao[] = [{ id: 123 }];
        expectedResult = service.addEdicaoToCollectionIfMissing(edicaoCollection, ...edicaoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const edicao: IEdicao = { id: 123 };
        const edicao2: IEdicao = { id: 456 };
        expectedResult = service.addEdicaoToCollectionIfMissing([], edicao, edicao2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(edicao);
        expect(expectedResult).toContain(edicao2);
      });

      it('should accept null and undefined values', () => {
        const edicao: IEdicao = { id: 123 };
        expectedResult = service.addEdicaoToCollectionIfMissing([], null, edicao, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(edicao);
      });

      it('should return initial array if no Edicao is added', () => {
        const edicaoCollection: IEdicao[] = [{ id: 123 }];
        expectedResult = service.addEdicaoToCollectionIfMissing(edicaoCollection, undefined, null);
        expect(expectedResult).toEqual(edicaoCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
