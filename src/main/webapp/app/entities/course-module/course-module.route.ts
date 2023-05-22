import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ICourseModule, CourseModule } from 'app/shared/model/course-module.model';
import { CourseModuleService } from './course-module.service';
import { CourseModuleComponent } from './course-module.component';
import { CourseModuleDetailComponent } from './course-module-detail.component';
import { CourseModuleUpdateComponent } from './course-module-update.component';

@Injectable({ providedIn: 'root' })
export class CourseModuleResolve implements Resolve<ICourseModule> {
  constructor(private service: CourseModuleService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICourseModule> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((courseModule: HttpResponse<CourseModule>) => {
          if (courseModule.body) {
            return of(courseModule.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CourseModule());
  }
}

export const courseModuleRoute: Routes = [
  {
    path: '',
    component: CourseModuleComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'eduOnlineApp.courseModule.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CourseModuleDetailComponent,
    resolve: {
      courseModule: CourseModuleResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'eduOnlineApp.courseModule.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CourseModuleUpdateComponent,
    resolve: {
      courseModule: CourseModuleResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'eduOnlineApp.courseModule.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CourseModuleUpdateComponent,
    resolve: {
      courseModule: CourseModuleResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'eduOnlineApp.courseModule.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
