import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IPlay, Play } from '../play.model';

import { PlayService } from './play.service';

describe('Play Service', () => {
  let service: PlayService;
  let httpMock: HttpTestingController;
  let elemDefault: IPlay;
  let expectedResult: IPlay | IPlay[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PlayService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      playData: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          playData: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Play', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          playData: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          playData: currentDate,
        },
        returnedFromService
      );

      service.create(new Play()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Play', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          playData: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          playData: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Play', () => {
      const patchObject = Object.assign(
        {
          playData: currentDate.format(DATE_TIME_FORMAT),
        },
        new Play()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          playData: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Play', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          playData: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          playData: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Play', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPlayToCollectionIfMissing', () => {
      it('should add a Play to an empty array', () => {
        const play: IPlay = { id: 123 };
        expectedResult = service.addPlayToCollectionIfMissing([], play);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(play);
      });

      it('should not add a Play to an array that contains it', () => {
        const play: IPlay = { id: 123 };
        const playCollection: IPlay[] = [
          {
            ...play,
          },
          { id: 456 },
        ];
        expectedResult = service.addPlayToCollectionIfMissing(playCollection, play);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Play to an array that doesn't contain it", () => {
        const play: IPlay = { id: 123 };
        const playCollection: IPlay[] = [{ id: 456 }];
        expectedResult = service.addPlayToCollectionIfMissing(playCollection, play);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(play);
      });

      it('should add only unique Play to an array', () => {
        const playArray: IPlay[] = [{ id: 123 }, { id: 456 }, { id: 97405 }];
        const playCollection: IPlay[] = [{ id: 123 }];
        expectedResult = service.addPlayToCollectionIfMissing(playCollection, ...playArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const play: IPlay = { id: 123 };
        const play2: IPlay = { id: 456 };
        expectedResult = service.addPlayToCollectionIfMissing([], play, play2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(play);
        expect(expectedResult).toContain(play2);
      });

      it('should accept null and undefined values', () => {
        const play: IPlay = { id: 123 };
        expectedResult = service.addPlayToCollectionIfMissing([], null, play, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(play);
      });

      it('should return initial array if no Play is added', () => {
        const playCollection: IPlay[] = [{ id: 123 }];
        expectedResult = service.addPlayToCollectionIfMissing(playCollection, undefined, null);
        expect(expectedResult).toEqual(playCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
