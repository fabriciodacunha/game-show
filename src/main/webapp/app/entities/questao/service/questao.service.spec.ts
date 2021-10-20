import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { Assunto } from 'app/entities/enumerations/assunto.model';
import { Alternativa } from 'app/entities/enumerations/alternativa.model';
import { IQuestao, Questao } from '../questao.model';

import { QuestaoService } from './questao.service';

describe('Questao Service', () => {
  let service: QuestaoService;
  let httpMock: HttpTestingController;
  let elemDefault: IQuestao;
  let expectedResult: IQuestao | IQuestao[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(QuestaoService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      questaoTitulo: 'AAAAAAA',
      alternativaA: 'AAAAAAA',
      alternativaB: 'AAAAAAA',
      alternativaC: 'AAAAAAA',
      alternativaD: 'AAAAAAA',
      nivelIdade: 0,
      assunto: Assunto.PORTUGUES,
      alternativaCerta: Alternativa.A,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Questao', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Questao()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Questao', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          questaoTitulo: 'BBBBBB',
          alternativaA: 'BBBBBB',
          alternativaB: 'BBBBBB',
          alternativaC: 'BBBBBB',
          alternativaD: 'BBBBBB',
          nivelIdade: 1,
          assunto: 'BBBBBB',
          alternativaCerta: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Questao', () => {
      const patchObject = Object.assign(
        {
          alternativaB: 'BBBBBB',
          alternativaD: 'BBBBBB',
          nivelIdade: 1,
          assunto: 'BBBBBB',
        },
        new Questao()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Questao', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          questaoTitulo: 'BBBBBB',
          alternativaA: 'BBBBBB',
          alternativaB: 'BBBBBB',
          alternativaC: 'BBBBBB',
          alternativaD: 'BBBBBB',
          nivelIdade: 1,
          assunto: 'BBBBBB',
          alternativaCerta: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Questao', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addQuestaoToCollectionIfMissing', () => {
      it('should add a Questao to an empty array', () => {
        const questao: IQuestao = { id: 123 };
        expectedResult = service.addQuestaoToCollectionIfMissing([], questao);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(questao);
      });

      it('should not add a Questao to an array that contains it', () => {
        const questao: IQuestao = { id: 123 };
        const questaoCollection: IQuestao[] = [
          {
            ...questao,
          },
          { id: 456 },
        ];
        expectedResult = service.addQuestaoToCollectionIfMissing(questaoCollection, questao);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Questao to an array that doesn't contain it", () => {
        const questao: IQuestao = { id: 123 };
        const questaoCollection: IQuestao[] = [{ id: 456 }];
        expectedResult = service.addQuestaoToCollectionIfMissing(questaoCollection, questao);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(questao);
      });

      it('should add only unique Questao to an array', () => {
        const questaoArray: IQuestao[] = [{ id: 123 }, { id: 456 }, { id: 8531 }];
        const questaoCollection: IQuestao[] = [{ id: 123 }];
        expectedResult = service.addQuestaoToCollectionIfMissing(questaoCollection, ...questaoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const questao: IQuestao = { id: 123 };
        const questao2: IQuestao = { id: 456 };
        expectedResult = service.addQuestaoToCollectionIfMissing([], questao, questao2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(questao);
        expect(expectedResult).toContain(questao2);
      });

      it('should accept null and undefined values', () => {
        const questao: IQuestao = { id: 123 };
        expectedResult = service.addQuestaoToCollectionIfMissing([], null, questao, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(questao);
      });

      it('should return initial array if no Questao is added', () => {
        const questaoCollection: IQuestao[] = [{ id: 123 }];
        expectedResult = service.addQuestaoToCollectionIfMissing(questaoCollection, undefined, null);
        expect(expectedResult).toEqual(questaoCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
